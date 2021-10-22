package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.data.LIGHT_THEME
import com.vau.studio.iosstyle.idialer_phone.data.NAV_SCREEN_KEY
import com.vau.studio.iosstyle.idialer_phone.data.THEME_KEY
import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import com.vau.studio.iosstyle.idialer_phone.data.models.AppRoute
import com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen.HomeScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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

    init {
        initNavScreen()
        initTheme()
    }

    fun navigateTo(route: String, args: Map<String, Any>? = null) {
        if (HomeScreen.homeScreenRoutes.contains(route)) {
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

    private fun initTheme() {
        _appTheme.value = sharePreClient.getInt(THEME_KEY)
    }

    private fun initNavScreen() {
        val preScreen = sharePreClient.getString(NAV_SCREEN_KEY) ?: DEFAULT_SCREEN_NAME
        _mainRoute.value = AppRoute(preScreen)
    }

}