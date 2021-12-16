package com.vau.studio.iosstyle.idialer_phone.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.TelecomManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import com.vau.studio.iosstyle.idialer_phone.BuildConfig
import com.vau.studio.iosstyle.idialer_phone.data.REQUEST_CODE_FOR_DIALER
import java.lang.IllegalStateException

/**
 * Activity result for pick a default dialer app
 */
class PickDefaultActivityContract : ActivityResultContract<Int, Int?>() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun createIntent(context: Context, input: Int?): Intent {
        if (input == REQUEST_CODE_FOR_DIALER) {
            return Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(
                    TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                    BuildConfig.APPLICATION_ID
                )
            }
        }

        throw IllegalStateException("PickDefaultActivityResult only configs with DIALER, please specify more if you want more")
    }

    /**
     * return result code
     */
    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}