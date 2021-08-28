package com.vau.studio.iosstyle.idialer_phone.views.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vau.studio.iosstyle.idialer_phone.data.DARK_THEME

@Composable
fun AppTheme(
    theme: Int,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        systemUiController.apply {
            setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
            setStatusBarColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }

    return MaterialTheme(
        colors = if (theme == DARK_THEME) DarkColor else LightColor
    ) {
        content()
    }
}

val iosBlue = Color(0xFF007AFF)
val iosGreen = Color(0xFF53d769)
val iosRed = Color(0xFFfc3d39)
val iosBlack = Color(0xFF000000)
val iosWhite = Color(0xFFFFFFFF)

private val DarkColor = darkColors(
    primary = iosBlue,
    secondary = iosGreen,
    error = iosRed,
    background = iosBlack,
    surface = iosBlack
)

private val LightColor = lightColors(
    primary = iosBlue,
    secondary = iosGreen,
    error = iosRed,
    background = iosWhite,
    surface = iosWhite
)