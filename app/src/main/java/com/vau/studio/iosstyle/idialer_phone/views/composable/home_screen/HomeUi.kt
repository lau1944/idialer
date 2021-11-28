package com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.vau.studio.iosstyle.idialer_phone.data.DEFAULT_SCREEN_NAME
import com.vau.studio.iosstyle.idialer_phone.data.models.AppRoute
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen.CONTACT_DETAIL_ROUTE
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen.ContactDetailUi
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen.QUERY_PARAM_FIX
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen.ContactUi
import com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen.FavoriteScreen
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.DialerScreen
import com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen.RecentUi
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.*

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = viewModel(),
    contactViewModel: ContactViewModel,
    contactDetailViewModel: ContactDetailViewModel,
    callViewModel: CallViewModel,
    favoriteViewModel: FavoriteViewModel
) {
    val navController = rememberNavController()
    val mainScreen: AppRoute by mainViewModel.mainRoute.observeAsState(
        AppRoute(
            DEFAULT_SCREEN_NAME
        )
    )
    val secondRoute: List<AppRoute> by mainViewModel.secondRoute.observeAsState(arrayListOf())
    val nextRoute = secondRoute.lastOrNull()
    val popBack: Boolean by mainViewModel.popBack.observeAsState(false)

    LaunchedEffect(mainScreen) {
        navController.navigate(mainScreen.route)
    }

    LaunchedEffect(nextRoute) {
        if (nextRoute != null) {
            navController.navigate(nextRoute.parseRoute())
        }
    }

    LaunchedEffect(popBack) {
        if (popBack) {
            navController.popBackStack()
            mainViewModel.popBackFinish()
        }
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController, onTap = { route ->
                mainViewModel.navigateTo(route = route)
            })
        }
    ) { padding ->
        ScreenContent(
            navController = navController,
            padding = padding,
            startRoute = mainScreen,
            contactViewModel = contactViewModel,
            callViewModel = callViewModel,
            favoriteViewModel = favoriteViewModel,
            mainViewModel = mainViewModel,
            contactDetailViewModel = contactDetailViewModel
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ScreenContent(
    navController: NavHostController,
    padding: PaddingValues,
    startRoute: AppRoute,
    contactViewModel: ContactViewModel,
    callViewModel: CallViewModel,
    favoriteViewModel: FavoriteViewModel,
    mainViewModel: MainViewModel,
    contactDetailViewModel: ContactDetailViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startRoute.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(HomeScreen.FavoriteScreen.route) {
            FavoriteScreen(
                favoriteViewModel = favoriteViewModel,
                contactViewModel = contactViewModel,
                mainViewModel = mainViewModel
            )
        }
        composable(HomeScreen.RecentScreen.route) {
            RecentUi(
                callViewModel = callViewModel,
                mainViewModel = mainViewModel
            )
        }
        composable(HomeScreen.ContactScreen.route) {
            ContactUi(
                contactViewModel = contactViewModel,
                contactDetailViewModel = contactDetailViewModel,
                mainViewModel = mainViewModel
            )
        }
        composable(HomeScreen.KeypadScreen.route) { DialerScreen() }
        composable(HomeScreen.SettingScreen.route) { Text("Setting") }
        composable(
            CONTACT_DETAIL_ROUTE + QUERY_PARAM_FIX, arguments = listOf(
                navArgument("number")   { defaultValue = "" },
                navArgument("prevName") { defaultValue = "" },
                navArgument("id")       { defaultValue = "" }
            ),
        ) { entry ->
            ContactDetailUi(
                number = entry.arguments?.getString("number") ?: "0",
                preName = entry.arguments?.getString("prevName"),
                id = entry.arguments?.getString("id"),
                mainViewModel = mainViewModel,
                contactViewModel = contactViewModel,
                contactDetailViewModel = contactDetailViewModel,
                favoriteViewModel = favoriteViewModel
            )
        }
    }
}