package com.vau.studio.iosstyle.idialer_phone.data.repositories

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.BlockedNumberContract
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import contacts.async.commitAsync
import contacts.core.Contacts
import contacts.core.Insert
import contacts.core.entities.MutableAddress
import contacts.core.entities.MutableEmail
import contacts.core.entities.MutableName
import contacts.core.util.addAddress
import contacts.core.util.addEmail
import contacts.core.util.setName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalStateException

/**
 * This repo contains methods to get phone related data asynchronously
 */
object PhoneRepository {

    private const val TAG: String = "PhoneRepository"

    /**
     * Insert a new contact
     */
    suspend fun createNewContact(
        @ApplicationContext context: Context,
        contact: Contact
    ): Insert.Result {
        val insertResult = Contacts(context)
            .insert()
            .rawContact {
                setName {
                    this.displayName = contact.name
                }
                if (!contact.email.isNullOrEmpty()) {
                    addEmail {
                        this.address = contact.email
                    }
                }
                if (!contact.location.isNullOrEmpty()) {
                    addAddress {
                        this.formattedAddress = contact.location
                    }
                }
            }
            .allowBlanks(true)
            .commitAsync(Dispatchers.IO).await()
        return insertResult
    }

    /**
     * Get all contacts
     */
    fun getContactNames(
        @ApplicationContext context: Context,
        lookUp: String?
    ): Flow<List<Contact>?> = flow {

        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.StructuredPostal.DATA
            ),
            lookUp,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null) {
            val contacts = mutableListOf<Contact>()
            val contactIdSet: HashSet<String> = hashSetOf()

            try {
                while (cursor.moveToNext()) {
                    val nameIndex =
                        cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        )
                    val emailIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                    val numberIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val idIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    val phoneUrlIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                    val postalIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA)

                    val id = cursor.getString(idIndex)
                    if (!contactIdSet.contains(id)) {
                        contactIdSet.add(id)
                        contacts.add(
                            Contact(
                                name = cursor.getString(nameIndex),
                                email = cursor.getString(emailIndex),
                                number = cursor.getString(numberIndex).toLongOrNull(),
                                contactId = id,
                                phoneUrl = cursor.getString(phoneUrlIndex),
                                postal = cursor.getString(postalIndex)
                            )
                        )
                    }
                }
                emit(contacts)
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
                throw IllegalStateException(e)
            } finally {
                cursor.close()
            }
        } else {
            withContext(Dispatchers.Default) {
                emit(emptyList<Contact>())
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Get call logs
     */
    @SuppressLint("Recycle")
    fun getCallLog(
        @ApplicationContext context: Context,
        projection: Array<String>? = null,
        selection: String? = null
    ): Flow<List<Contact>?> = flow {

        val cursor =
            context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection, selection, null, CallLog.Calls.DATE + " DESC"
            )
        if (cursor != null) {
            try {
                val callLogs = mutableListOf<Contact>()
                val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                val name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
                val date = cursor.getColumnIndex(CallLog.Calls.DATE)
                val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
                val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    cursor.getColumnIndex(CallLog.Calls.LOCATION)
                } else {
                    null
                }
                while (cursor.moveToNext()) {
                    val contact = Contact(
                        name = cursor.getString(name),
                        number = cursor.getString(number).toLong(),
                        type = cursor.getString(type).toInt(),
                        callDate = cursor.getString(date),
                        duration = cursor.getString(duration),
                        location = if (location != null) cursor.getString(location) else ""
                    )
                    callLogs.add(contact)
                }
                emit(callLogs)
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
                throw IllegalStateException(e)
            } finally {
                cursor.close()
            }
        } else {
            emit(emptyList<Contact>())
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Delete call logs
     * * if call id equal to null, then would delete all the call logs in default
     */
    fun deleteCallLog(
        @ApplicationContext context: Context,
        id: String?
    ) {
        val queryString = "DATE=$id"
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI,
            if (id != null) queryString else null,
            null
        )
    }

    /**
     * Get all blocked number
     */
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getBlockNumber(
        @ApplicationContext context: Context
    ): Flow<List<String>> = flow {
        val cursor = context.contentResolver.query(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            arrayOf<String>(
                BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
            ), null, null, null
        )
        try {
            val numbers = arrayListOf<String>()
            val numIndex =
                cursor!!.getColumnIndex(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER)
            while (cursor.moveToNext()) {
                val number = cursor.getStringOrNull(numIndex)
                if (number != null) numbers.add(number)
            }
            emit(numbers)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        } finally {
            cursor?.close()
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Add block number
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun addBlockNumber(
        @ApplicationContext context: Context,
        number: String
    ) {
        val contentValue = ContentValues()
        contentValue.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        context.contentResolver.insert(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            contentValue
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteBlockNumber(
        @ApplicationContext context: Context,
        number: String
    ) {
        val contentValue = ContentValues()
        contentValue.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        val uri = context.contentResolver.insert(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            contentValue
        )
        context.contentResolver.delete(uri!!, null, null)
    }
}