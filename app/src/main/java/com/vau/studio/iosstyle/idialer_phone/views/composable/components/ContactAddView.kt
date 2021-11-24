package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.views.composable.*

@Composable
fun ContactAddView(contact: Contact, onCancel: () -> Unit, onDone: () -> Unit) {
    Box(
        Modifier
            .background(backgroundGray)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            ContactHeaderView(onCancel, onDone)
            ContactEditList(contact = contact)

        }
    }
}

@Composable
private fun ContactEditList(contact: Contact) {
    LazyColumn(content = {
        item {
            ContactEditAvatar()
            TextInfoSection()

            // phone field
            if (contact.number != null) {
                InfoRemoveView(hint = "phone", content = contact.number.toString())
            } else {
                InfoAddView(addHint = "phone")
            }

            // email field
            if (!contact.email.isNullOrEmpty()) {
                InfoRemoveView(hint = "email", content = contact.email)
            } else {
                InfoAddView(addHint = "email")
            }

            // address field
            if (!contact.location.isNullOrEmpty()) {
                InfoRemoveView(hint = "address", content = contact.location)
            } else {
                InfoAddView(addHint = "address")
            }
        }
    })
}

@Composable
private fun TextInfoSection() {
    Column(modifier = Modifier.padding(vertical = 15.dp)) {
        EditView {
            StandardEditText(hint = "Name")
        }
    }
}

@Composable
private fun ContactEditAvatar() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.Center) {
            AssetImage(
                res = R.drawable.ic_big_user,
                size = 75,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
            Text("add photo", style = TextStyle(color = iosBlue))
        }
    }
}

@Composable
private fun InfoRemoveView(hint: String, content: String, onRemove: (() -> Unit)? = null) {
    EditView(
        modifier = Modifier.clickable {
            onRemove?.invoke()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(appColor().background)
                .padding(vertical = 15.dp)
        ) {
            AssetImage(
                res = R.drawable.ic_remove_in_contact,
                size = 20,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Row() {
                Text(
                    hint,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(content, style = TextStyle(fontSize = 16.sp, color = iosBlue))
            }
        }
    }
}

@Composable
private fun InfoAddView(addHint: String, onAdd: (() -> Unit)? = null) {
    EditView(
        modifier = Modifier
            .clickable {
                onAdd?.invoke()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(appColor().background)
                .padding(vertical = 15.dp)
        ) {
            AssetImage(
                res = R.drawable.ic_add_in_contact,
                size = 20,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(addHint, style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
private fun ContactHeaderView(onDismiss: () -> Unit, onDone: () -> Unit) {
    val showAlertDialog = remember {
        mutableStateOf(false)
    }

    if (showAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { showAlertDialog.value = false },
            title = {
                Text(text = "Confirmation")
            },
            text = {
                Text("Are you sure you want to discard this new contact")
            },
            confirmButton = {
                Text(
                    "Discard Changes",
                    style = TextStyle(color = iosRed, fontSize = 16.sp),
                    modifier = Modifier.clickable {
                        showAlertDialog.value = false
                        onDismiss()
                    }.padding(horizontal = 5.dp))
            },
            dismissButton = {
                Text(
                    "Keep Editing",
                    style = TextStyle(color = iosBlue, fontSize = 16.sp),
                    modifier = Modifier.clickable {
                        showAlertDialog.value = false
                    }.padding(horizontal = 5.dp))
            },
            backgroundColor = appColor().background
        )
    }

    Box(
        modifier = Modifier
            .height(45.dp)
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Cancel",
                style = TextStyle(color = iosBlue, fontSize = 16.sp),
                modifier = Modifier.clickable { showAlertDialog.value = true })
            Text(
                "New Contact",
                style = TextStyle(color = iosBlack, fontSize = 17.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            )
            Text(
                "Done",
                style = TextStyle(color = iosBlue, fontSize = 16.sp),
                modifier = Modifier.clickable { onDone() })
        }
    }
}

@Composable
private fun EditView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Divider(modifier = Modifier.background(iosGray.copy(0.1f)))
        Box(
            modifier = Modifier
        ) {
            content()
        }
        Divider(modifier = Modifier.background(iosGray.copy(0.1f)))
    }
}