package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.data.LIGHT_THEME
import com.vau.studio.iosstyle.idialer_phone.data.NAV_SCREEN_KEY
import com.vau.studio.iosstyle.idialer_phone.data.THEME_KEY
import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharePreClient: SharePreferenceClient
): ViewModel() {

    private val _navScreen = MutableLiveData(DEFAULT_SCREEN_NAME)
    val navScreen : LiveData<String> get() = _navScreen

    private val _appTheme = MutableLiveData(LIGHT_THEME)
    val appTheme : LiveData<Int> get() = _appTheme

    init {
        initNavScreen()
        initTheme()
    }

    fun navigateTo(route: String) {
        _navScreen.value = route
        sharePreClient.setString(NAV_SCREEN_KEY, route)
    }

    private fun initTheme() {
        _appTheme.value = sharePreClient.getInt(THEME_KEY)
    }

    private fun initNavScreen() {
        val preScreen = sharePreClient.getString(NAV_SCREEN_KEY) ?: DEFAULT_SCREEN_NAME
        _navScreen.value = preScreen
    }

}