package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.FavoriteViewModel

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun FavoriteScreen(favoriteViewModel: FavoriteViewModel, contactViewModel: ContactViewModel) {
    val isAddDialogShowed = remember {
        mutableStateOf(false)
    }

    if (isAddDialogShowed.value) {
        ShowDialog(contactViewModel, onDismiss = {
            isAddDialogShowed.value = false
        }, onSelect = { contact ->
            favoriteViewModel.addToFavorite(contact)
            isAddDialogShowed.value = false
        })
    }

    Scaffold(
        topBar = {
            FavoriteAppbar(favoriteViewModel = favoriteViewModel, onAdded = {
                isAddDialogShowed.value = true
            })
        }
    ) {
        val favoriteState by favoriteViewModel.contactListState.observeAsState()

        UiProgressLayout(state = favoriteState) {
            val favoriteList = (favoriteState as UiState.Success).data as List<Contact>
            if (favoriteList.isNullOrEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("No Favorites", style = TextStyle(color = Color.Gray.copy(alpha = 0.5f)))
                }
            } else {
                FavoriteList(contacts = favoriteList)
            }
        }
    }
}

@Composable
private fun FavoriteList(contacts: List<Contact>) {
    LazyColumn(content = {
        items(contacts,
            itemContent = { contact ->
                FavoriteItem(contact = contact)
            })
    })
}

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
private fun ShowDialog(
    contactViewModel: ContactViewModel,
    onSelect: (Contact) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        FavoriteSelectionView(onSelected = onSelect, contactViewModel = contactViewModel) {
            onDismiss()
        }
    }
}