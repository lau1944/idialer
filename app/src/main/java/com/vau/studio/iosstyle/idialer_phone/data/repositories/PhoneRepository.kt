package com.vau.studio.iosstyle.idialer_phone.data.repositories

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
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
    fun getContactNames(context: Context, lookUp: String?) : Flow<List<String>?> = flow {
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
                withContext(Dispatchers.Default) {
                    emit(contacts)
                }
                cursor.close()
            }
        }
    }
}