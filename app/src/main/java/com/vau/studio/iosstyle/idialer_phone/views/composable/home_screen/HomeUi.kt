package com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel = viewModel()) {
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
        ScreenContent(navController = navController, padding = padding, startRoute = currentScreen)
    }
}

@Composable
fun ScreenContent(navController: NavHostController, padding: PaddingValues, startRoute: String) {
    NavHost(
        navController = navController,
        startDestination = startRoute,
        Modifier.padding(padding)
    ) {
        composable(HomeScreen.FavoriteScreen.route) { Text("hello") }
        composable(HomeScreen.RecentScreen.route) { Text("hello") }
        composable(HomeScreen.ContactScreen.route) { Text("hello") }
        composable(HomeScreen.KeypadScreen.route) { Text("hello") }
        composable(HomeScreen.VoiceMailScreen.route) { Text("hello") }
    }
}