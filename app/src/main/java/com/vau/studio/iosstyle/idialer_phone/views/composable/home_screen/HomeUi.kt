package com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_WRITE_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen.ContactUi
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.DialerScreen
import com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen.RecentUi
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = viewModel(),
    contactViewModel: ContactViewModel,
    callViewModel: CallViewModel
) {
    val navController = rememberNavController()
    val currentScreen: String by mainViewModel.navScreen.observeAsState(DEFAULT_SCREEN_NAME)

    LaunchedEffect(currentScreen) {
        navController.navigate(currentScreen)
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController, onTap = { route ->
                mainViewModel.navigateTo(route)
            })
        }
    ) { padding ->
        ScreenContent(
            navController = navController,
            padding = padding,
            startRoute = currentScreen,
            contactViewModel = contactViewModel,
            callViewModel = callViewModel
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ScreenContent(
    navController: NavHostController,
    padding: PaddingValues,
    startRoute: String,
    contactViewModel: ContactViewModel,
    callViewModel: CallViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startRoute,
        Modifier.padding(padding)
    ) {
        composable(HomeScreen.FavoriteScreen.route) { Text("hello") }
        composable(HomeScreen.RecentScreen.route) { RecentUi(callViewModel = callViewModel) }
        composable(HomeScreen.ContactScreen.route) {
            ContactUi(
                contactViewModel = contactViewModel
            )
        }
        composable(HomeScreen.KeypadScreen.route) { DialerScreen() }
        composable(HomeScreen.VoiceMailScreen.route) { Text("hello") }
    }
}