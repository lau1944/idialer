package com.vau.studio.iosstyle.idialer_phone

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.LIGHT_THEME
import com.vau.studio.iosstyle.idialer_phone.views.composable.AppTheme
import com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen.HomeScreen
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.FavoriteViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val contactViewModel: ContactViewModel by viewModels()
    private val callViewModel: CallViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val theme: Int by mainViewModel.appTheme.observeAsState(initial = LIGHT_THEME)

            // init device info
            val current = LocalConfiguration.current
            DeviceUtil.setInfo(DeviceInfo(
                width = current.screenWidthDp,
                height = current.screenHeightDp
            ))

            AppTheme(theme = theme) {
                HomeScreen(
                    mainViewModel = mainViewModel,
                    contactViewModel = contactViewModel,
                    callViewModel = callViewModel,
                    favoriteViewModel = favoriteViewModel
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
}