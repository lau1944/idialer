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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

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
    ): Flow<List<String>?> = flow {
        withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ),
                lookUp,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            if (cursor != null) {
                val contacts = mutableListOf<String>()
                while (cursor.moveToNext()) {
                    val nameIndex =
                        cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        )
                    if (nameIndex > -1) {
                        contacts.add(cursor.getStringOrNull(nameIndex) ?: "")
                    }
                }
                cursor.close()

                withContext(Dispatchers.Default) {
                    emit(contacts)
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
    ): Flow<List<CallHistory>> = flow {
        withContext(Dispatchers.IO) {
            val cursor =
                context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    null, null, null, CallLog.Calls.DATE + " DESC"
                )
            if (cursor != null) {
                val callLogs = mutableListOf<CallHistory>()
                val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                val name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
                val date = cursor.getColumnIndex(CallLog.Calls.DATE)
                val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
                while (cursor.moveToNext()) {
                    val callHistory = CallHistory(
                        name = cursor.getString(name),
                        number = cursor.getString(number),
                        type = cursor.getString(type),
                        date = cursor.getString(date),
                        duration = cursor.getString(duration)
                    )
                    callLogs.add(callHistory)
                }
                cursor.close()

                withContext(Dispatchers.Default) {
                    emit(callLogs)
                }
            }
        }
    }
}