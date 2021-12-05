package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.os.Build
import android.telecom.TelecomManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vau.studio.iosstyle.idialer_phone.BuildConfig
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.data.LIGHT_THEME
import com.vau.studio.iosstyle.idialer_phone.data.NAV_SCREEN_KEY
import com.vau.studio.iosstyle.idialer_phone.data.THEME_KEY
import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import com.vau.studio.iosstyle.idialer_phone.data.models.AppRoute
import com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharePreClient: SharePreferenceClient
) : ViewModel() {

    private val _mainRoute = MutableLiveData(AppRoute(DEFAULT_SCREEN_NAME))
    val mainRoute: LiveData<AppRoute> get() = _mainRoute

    private val _secondRoute = MutableLiveData(arrayListOf<AppRoute>())
    val secondRoute: MutableLiveData<ArrayList<AppRoute>> get() = _secondRoute

    private val _appTheme = MutableLiveData(LIGHT_THEME)
    val appTheme: LiveData<Int> get() = _appTheme

    private val _popBack = MutableLiveData(false)
    val popBack: LiveData<Boolean> get() = _popBack

    private val _isDefaultCaller = MutableLiveData<Boolean>(false)
    val isDefaultCaller : LiveData<Boolean> get() = _isDefaultCaller

    init {
        initNavScreen()
        initTheme()
        checkIfDefaultCaller()
    }

    fun navigateTo(route: String, args: Map<String, Any>? = null) {
        if (Screen.homeScreenRoutes.contains(route)) {
            sharePreClient.setString(NAV_SCREEN_KEY, route)
            _mainRoute.value = AppRoute(route = route, args = args)
        } else {
            val newRoute = arrayListOf<AppRoute>().apply {
                addAll(_secondRoute.value!!)
                add(AppRoute(route = route, args = args))
            }
            _secondRoute.value = newRoute
        }
    }

    fun popBack() {
        if (_secondRoute.value.isNullOrEmpty())
            throw IllegalStateException("Only contains second route has the access to pop back route")

        val newRoute = arrayListOf<AppRoute>().apply {
            addAll(_secondRoute.value!!)
            removeLastOrNull()
        }
        _secondRoute.value = newRoute
        _popBack.value = true
    }

    fun popBackFinish() {
        _popBack.value = false
    }

    fun checkIfDefaultCaller() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val telManager = context.getSystemService(TELECOM_SERVICE) as TelecomManager
        _isDefaultCaller.value = telManager.defaultDialerPackage == BuildConfig.APPLICATION_ID
    }

    private fun initTheme() {
        _appTheme.value = sharePreClient.getInt(THEME_KEY)
    }

    private fun initNavScreen() {
        val preScreen = sharePreClient.getString(NAV_SCREEN_KEY) ?: DEFAULT_SCREEN_NAME
        _mainRoute.value = AppRoute(preScreen)
    }

}