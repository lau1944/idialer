package com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vau.studio.iosstyle.idialer_phone.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun AppBottomBar(navController: NavController, onTap: (route: String) -> Unit) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val items = HomeScreen.screensList()
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = screen.drawableId),
                        contentDescription = screen.route,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                  Text(
                      stringResource(id = screen.resId),
                      textAlign = TextAlign.Center,
                      fontSize = 11.sp
                  )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color(android.graphics.Color.GRAY),
                onClick = {
                    onTap(screen.route)
                })
        }
    }
}

enum class HomeScreen(
    val route: String,
    @StringRes val resId: Int,
    @DrawableRes val drawableId: Int
) {
    FavoriteScreen("favorite", R.string.favorites, R.drawable.ic_star),
    RecentScreen("recent", R.string.recents, R.drawable.ic_clock),
    ContactScreen("contact", R.string.contacts, R.drawable.ic_user),
    KeypadScreen("keypad", R.string.Keypad, R.drawable.ic_keypad),
    VoiceMailScreen("voice_mail", R.string.voicemail, R.drawable.ic_voicemail);

    companion object {
        @JvmStatic
        fun screensList(): List<HomeScreen> {
            return listOf(
                FavoriteScreen,
                RecentScreen,
                ContactScreen,
                KeypadScreen,
                VoiceMailScreen
            )
        }
    }
}
