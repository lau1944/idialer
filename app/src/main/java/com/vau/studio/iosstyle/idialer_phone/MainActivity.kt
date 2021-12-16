package com.vau.studio.iosstyle.idialer_phone

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.vau.studio.iosstyle.idialer_phone.core.DeviceInfo
import com.vau.studio.iosstyle.idialer_phone.core.DeviceUtil
import com.vau.studio.iosstyle.idialer_phone.core.PickDefaultActivityContract
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.LIGHT_THEME
import com.vau.studio.iosstyle.idialer_phone.data.REQUEST_CODE_FOR_DIALER
import com.vau.studio.iosstyle.idialer_phone.views.composable.AppTheme
import com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen.HomeScreen
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val contactViewModel: ContactViewModel by viewModels()
    private val callViewModel: CallViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val contactDetailViewModel: ContactDetailViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerResultLauncher()
        //observeDefaultDialer()

        setContent {
            val theme: Int by mainViewModel.appTheme.observeAsState(initial = LIGHT_THEME)

            // init device info
            val current = LocalConfiguration.current
            DeviceUtil.setInfo(
                DeviceInfo(
                    width = current.screenWidthDp,
                    height = current.screenHeightDp
                )
            )

            AppTheme(theme = theme) {
                HomeScreen(
                    mainViewModel = mainViewModel,
                    contactViewModel = contactViewModel,
                    callViewModel = callViewModel,
                    favoriteViewModel = favoriteViewModel,
                    contactDetailViewModel = contactDetailViewModel
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                this,
                CALL_LOG_READ_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callViewModel.getCallHistory()
        }
    }

    private fun registerResultLauncher() {
        val launcher = registerForActivityResult(PickDefaultActivityContract()) { result ->
            handleDefaultDialerResult(result)
        }
        launcher.launch(REQUEST_CODE_FOR_DIALER)
    }

    private fun handleDefaultDialerResult(resultCode: Int?) {
        when (resultCode) {
            RESULT_OK -> {
                mainViewModel.checkIfDefaultCaller()
                callSystemDialer()
                Toast.makeText(
                    this,
                    "iDialer has been added to your default dialer app",
                    Toast.LENGTH_SHORT
                ).show()
            }
            RESULT_CANCELED -> {
                /*Toast.makeText(
                    this,
                    "iDialer is not currently your default dialer app, some functionalities would not perform properly",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
            else -> {
                Toast.makeText(
                    this,
                    "Error occurred, please check again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Call dialer system api
     */
    private fun callSystemDialer() {
        contactViewModel.getBlockNumbers()
    }
}