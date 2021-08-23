package com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController, onTap = { route ->
                navController.navigate(route = route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = HomeScreen.FavoriteScreen.route,
            Modifier.padding(padding)
        ) {
            composable(HomeScreen.FavoriteScreen.route) { Text("hello") }
            composable(HomeScreen.RecentScreen.route) { Text("hello") }
            composable(HomeScreen.ContactScreen.route) { Text("hello") }
            composable(HomeScreen.KeypadScreen.route) { Text("hello") }
            composable(HomeScreen.VoiceMailScreen.route) { Text("hello") }
        }
    }
}