package com.vau.studio.iosstyle.idialer_phone.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object SharePreferenceClient {

    private lateinit var sharePreference: SharedPreferences

    fun init(context: Context, key: String) {
        sharePreference = context.getSharedPreferences(key, MODE_PRIVATE)
    }

    fun setString(key: String, value: String) {
        sharePreference.edit {
            this.putString(key, value)
        }
    }

    fun setInt(key: String, value: Int) {
        sharePreference.edit {
            this.putInt(key, value)
        }
    }

    fun getString(key: String): String? {
        if (sharePreference.contains(key))
            return sharePreference.getString(key, null)
        return null
    }

    fun getInt(key: String): Int {
        if (sharePreference.contains(key)) {
            return sharePreference.getInt(key, 0)
        }
        return 0
    }

}