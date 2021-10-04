package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.FavoriteViewModel

@Composable
fun FavoriteScreen(favoriteViewModel: FavoriteViewModel) {
    Scaffold(
        topBar = { FavoriteAppbar(favoriteViewModel = favoriteViewModel) }
    ) {

    }
}