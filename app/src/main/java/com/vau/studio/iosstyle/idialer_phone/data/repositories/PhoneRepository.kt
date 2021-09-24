package com.vau.studio.iosstyle.idialer_phone.data.repositories

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * This repo contains methods to get phone related data asynchronously
 */
object PhoneRepository {
    /**
     * Get all contacts
     */
    fun getContactNames(
        @ApplicationContext context: Context,
        lookUp: String?
    ): Flow<List<Contact>?> = flow {
        withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ),
                lookUp,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            if (cursor != null) {
                val contacts = mutableListOf<Contact>()
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
                    contacts.add(
                        Contact(
                            name = cursor.getString(nameIndex),
                            email = cursor.getString(emailIndex),
                            number = cursor.getString(numberIndex),
                            contactId = cursor.getString(idIndex),
                            phoneUrl = cursor.getString(phoneUrlIndex)
                        )
                    )
                }
                cursor.close()

                withContext(Dispatchers.Default) {
                    emit(contacts)
                }
            } else {
                withContext(Dispatchers.Default) {
                    emit(emptyList<Contact>())
                }
            }
        }
    }

    /**
     * Get call logs
     */
    @SuppressLint("Recycle")
    fun getCallLog(
        @ApplicationContext context: Context,
        projection: Array<String>
    ): Flow<List<CallHistory>?> = flow {
        withContext(Dispatchers.IO) {
            val cursor =
                context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    projection, null, null, CallLog.Calls.DATE + " DESC"
                )
            if (cursor != null) {
                val callLogs = mutableListOf<CallHistory>()
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
                    val callHistory = CallHistory(
                        name = cursor.getString(name),
                        number = cursor.getString(number),
                        type = cursor.getString(type).toInt(),
                        date = cursor.getString(date),
                        duration = cursor.getString(duration),
                        location = if (location != null) cursor.getString(location) else ""
                    )
                    callLogs.add(callHistory)
                }
                cursor.close()

                withContext(Dispatchers.Default) {
                    emit(callLogs)
                }
            } else {
                withContext(Dispatchers.Default) {
                    emit(emptyList<CallHistory>())
                }
            }
        }
    }

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
}