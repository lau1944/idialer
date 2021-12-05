package com.vau.studio.iosstyle.idialer_phone.data.repositories

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.BlockedNumberContract
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import contacts.async.commitAsync
import contacts.core.Fields.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalStateException
import android.provider.ContactsContract.RawContacts
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import contacts.core.*
import contacts.core.entities.*
import contacts.core.util.*
import contacts.core.entities.RawContact as RawContact


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
    ): Flow<Insert.Result> = flow {
        val insertResult = Contacts(context)
            .insert()
            .rawContact {
                setName {
                    this.displayName = contact.name
                }
                addEmail {
                    this.address = contact.email
                }
                addAddress {
                    this.formattedAddress = contact.location
                }

                if (!contact.number.isNullOrEmpty()) {
                    addPhone {
                        this.number = contact.number
                    }
                }
            }
            .allowBlanks(true)
            .commit()

        val photoBitmap = contact.getPhotoBitmap()
        if (photoBitmap != null) {
            insertResult.contacts(context = context).firstOrNull()?.setPhoto(context, photoBitmap)
        }
        emit(insertResult)
    }.flowOn(Dispatchers.IO)

    /**
     * Update contact with new info
     */
    fun updateContact(context: Context, contact: Contact): Flow<Update.Result> = flow {
        val queriedContact = findContacts(context)
        val index = queriedContact.indexOfFirst {
            it.id == contact.contactId?.toLong()
        }

        if (index == -1) throw IllegalStateException("This contact does not exist")

        val result = Contacts(context)
            .update()
            .contacts(queriedContact[index].toMutableContact().apply {
                setName {
                    this.displayName = contact.name
                }

                addEmail {
                    this.address = contact.email
                }
                addAddress {
                    this.formattedAddress = contact.location
                }

                if (!contact.number.isNullOrEmpty()) {
                    addPhone {
                        this.number = contact.number
                    }
                }
            })
            .commit()
        emit(result)

    }.flowOn(Dispatchers.IO)

    /**
     * Delete contact by id
     */
    fun deleteContactById(context: Context, contactId: String): Flow<UiState<*>> = flow {
        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        try {
            while (cursor!!.moveToNext()) {
                val idIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                if (cursor.getString(idIndex).equals(contactId)) {
                    val keyIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)
                    val lookupKey =
                        cursor.getString(keyIndex)
                    val uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        lookupKey
                    )
                    context.contentResolver.delete(uri, null, null)
                }
            }
            emit(UiState.Success<Nothing>())
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
            emit(UiState.Failed(e))
        } finally {
            cursor?.close()
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Get all contacts
     */
    fun getContactNames(
        @ApplicationContext context: Context,
        where: Where<AbstractDataField>?
    ): Flow<List<Contact>?> = flow {

        val queriedContacts = findContacts(context, where)
        val contacts = arrayListOf<Contact>()
        for (contact in queriedContacts) {
            if (!contact.displayNamePrimary.isNullOrEmpty()) {
                contacts.add(
                    Contact(
                        contactId = contact.id!!.toInt(),
                        phoneUrl = contact.photoThumbnailUri.toString(),
                        name = contact.displayNamePrimary ?: "unknown",
                        number = if (contact.phoneList()
                                .isNullOrEmpty()
                        ) "" else contact.phoneList()[0].number,
                        email = if (contact.emailList()
                                .isNullOrEmpty()
                        ) "" else contact.emailList()[0].address,
                    ).apply {
                        if (!this.phoneUrl.isNullOrEmpty()) {
                            this.setPhotoBitmap(contact.photoThumbnailBitmap(context))
                        }
                    }
                )

            }
        }
        emit(contacts)
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
                        number = cursor.getString(number),
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
        try {
            contentValue.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            context.contentResolver.insert(
                BlockedNumberContract.BlockedNumbers.CONTENT_URI,
                contentValue
            )
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteBlockNumber(
        @ApplicationContext context: Context,
        number: String
    ) {
        val contentValue = ContentValues()
        try {
            contentValue.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            val uri = context.contentResolver.insert(
                BlockedNumberContract.BlockedNumbers.CONTENT_URI,
                contentValue
            )
            context.contentResolver.delete(uri!!, null, null)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    private fun findContacts(
        context: Context,
        where: Where<AbstractDataField>? = null
    ): List<contacts.core.entities.Contact> {
        return Contacts(context)
            .query()
            .include(
                Fields.Contact.DisplayNamePrimary,
                Fields.Contact.DisplayNameAlt,
                Fields.Email.Address,
                Fields.Contact.HasPhoneNumber,
                Fields.Phone.Number,
                Fields.Contact.Id,
                Fields.Contact.PhotoUri,
                Fields.Contact.PhotoThumbnailUri
            )
            .where(where)
            .find()
    }
}