package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.BitmapUtils
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.ContactInputType
import com.vau.studio.iosstyle.idialer_phone.views.composable.*
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactDetailViewModel
import java.lang.Exception
import java.lang.NumberFormatException

@ExperimentalComposeUiApi
@Composable
fun ContactAddView(
    contactDetailViewModel: ContactDetailViewModel,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val contact by contactDetailViewModel.newContact.observeAsState()

    val showInputDialog = remember {
        mutableStateOf(false)
    }
    val contactInputType = remember {
        mutableStateOf<ContactInputType?>(null)
    }
    val selectedPhoto = remember {
        mutableStateOf<Uri?>(null)
    }

    Box(
        Modifier
            .background(backgroundGray)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            ContactHeaderView(onCancel, {
                if (contact?.name.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        "Name cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onDone()
                }
            })
            ContactEditList(
                !showInputDialog.value, contact = contact!!, contactDetailViewModel,
                onAddPhoto = {
                    contactInputType.value = ContactInputType.Photo
                    selectedPhoto.value = it
                    contactDetailViewModel.updateContact(
                        contact!!.copy(phoneUrl = it.toString()).apply {
                            this.setPhotoBitmap(BitmapUtils.uriToBitmap(context, it))
                        })
                },
                onAdd = {
                    contactInputType.value = it
                    showInputDialog.value = true
                },
                onRemoved = {
                    removeInfo(contact as Contact, it, contactDetailViewModel)
                }
            )
        }
    }

    if (showInputDialog.value) {
        TextEditDialogView(
            value = contact?.getFieldFromType(contactInputType.value) ?: "",
            title = "Please type the contact ${contactInputType.value}",
            onCancel = {
                showInputDialog.value = false
            },
            onConfirm = {
                when (contactInputType.value) {
                    ContactInputType.Address -> {
                        contactDetailViewModel.updateContact(contact = contact!!.copy(location = it))
                    }
                    ContactInputType.Mail -> {
                        contactDetailViewModel.updateContact(contact = contact!!.copy(email = it))
                    }
                    ContactInputType.Name -> {
                        contactDetailViewModel.updateContact(contact = contact!!.copy(name = it))
                    }
                    ContactInputType.Phone -> {
                        try {
                            contactDetailViewModel.updateContact(contact = contact!!.copy(number = it))
                        } catch (e: NumberFormatException) {
                            Toast.makeText(
                                context,
                                "Please type a valid number",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i("ContactAddView", e.toString())
                        } catch (e: Exception) {
                            Log.i("ContactAddView", e.toString())
                        }
                    }
                    ContactInputType.Photo -> {
                        contactDetailViewModel.updateContact(contact!!.copy(phoneUrl = selectedPhoto.value.toString()))
                    }
                }
                showInputDialog.value = false
            })
    }
}

private fun removeInfo(
    contact: Contact,
    contactInputType: ContactInputType,
    contactDetailViewModel: ContactDetailViewModel
) {
    when (contactInputType) {
        ContactInputType.Address -> {
            contactDetailViewModel.updateContact(contact = contact.copy(location = null))
        }
        ContactInputType.Mail -> {
            contactDetailViewModel.updateContact(contact = contact.copy(email = null))
        }
        ContactInputType.Name -> {
            contactDetailViewModel.updateContact(contact = contact.copy(name = null))
        }
        ContactInputType.Phone -> {
            contactDetailViewModel.updateContact(contact = contact.copy(number = ""))
        }
        ContactInputType.Photo -> {
            contactDetailViewModel.updateContact(contact = contact.copy(phoneUrl = null))
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun ContactEditList(
    isCardClickable: Boolean,
    contact: Contact,
    contactDetailViewModel: ContactDetailViewModel,
    onAdd: ((ContactInputType) -> Unit),
    onAddPhoto: (Uri?) -> Unit,
    onRemoved: (ContactInputType) -> Unit,
) {
    LazyColumn(content = {
        item {
            ContactEditAvatar(isCardClickable, onAdd = {
                onAddPhoto(it)
            })

            TextInfoSection(contact, contactDetailViewModel)

            // phone field
            if (!contact.number.isNullOrEmpty()) {
                InfoRemoveView(
                    isClickable = isCardClickable,
                    hint = "phone",
                    content = contact.number.toString(), onRemove = {
                        onRemoved(ContactInputType.Phone)
                    }, onEdit = {
                        onAdd(ContactInputType.Phone)
                    })
            } else {
                InfoAddView(isClickable = isCardClickable, addHint = "phone", onAdd = {
                    onAdd(ContactInputType.Phone)
                })
            }

            // email field
            if (!contact.email.isNullOrEmpty()) {
                InfoRemoveView(
                    isClickable = isCardClickable,
                    hint = "email",
                    content = contact.email,
                    onRemove = {
                        onRemoved(ContactInputType.Mail)
                    },
                    onEdit = {
                        onAdd(ContactInputType.Mail)
                    })
            } else {
                InfoAddView(isClickable = isCardClickable, addHint = "email", onAdd = {
                    onAdd(ContactInputType.Mail)
                })
            }

            // address field
            if (!contact.location.isNullOrEmpty()) {
                InfoRemoveView(
                    isClickable = isCardClickable,
                    hint = "address",
                    content = contact.location,
                    onRemove = {
                        onRemoved(ContactInputType.Address)
                    },
                    onEdit = {
                        onAdd(ContactInputType.Address)
                    })
            } else {
                InfoAddView(isClickable = isCardClickable, addHint = "address", onAdd = {
                    onAdd(ContactInputType.Address)
                })
            }
        }
    })
}

@ExperimentalComposeUiApi
@Composable
private fun TextInfoSection(contact: Contact, contactDetailViewModel: ContactDetailViewModel) {
    Column(modifier = Modifier.padding(vertical = 15.dp)) {
        EditView {
            StandardEditText(value = contact.name ?: "", hint = "Name", onChange = { text ->
                val newContact = contact.copy(name = text)
                contactDetailViewModel.updateContact(contact = newContact)
            })
        }
    }
}

@Composable
private fun ContactEditAvatar(isClickable: Boolean, onAdd: (Uri?) -> Unit) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        onAdd(imageUri)
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                enabled = isClickable,
                onClick = {
                    launcher.launch("image/*")
                }
            )
        ) {
            if (imageUri == null) {
                AssetImage(
                    res = R.drawable.ic_big_user,
                    size = 75,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            } else {
                GlideImage(
                    image = imageUri!!,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                )
            }
            Text(if (imageUri == null) "add photo" else "edit", style = TextStyle(color = iosBlue))
        }
    }
}

@Composable
private fun InfoRemoveView(
    isClickable: Boolean,
    hint: String,
    content: String,
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    EditView {
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
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {
                        onRemove?.invoke()
                    }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    enabled = isClickable,
                    onClick = {
                        onEdit?.invoke()
                    }
                )
            ) {
                Text(
                    hint,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    content,
                    style = TextStyle(fontSize = 16.sp, color = iosBlue)
                )
            }
        }
    }
}

@Composable
private fun InfoAddView(isClickable: Boolean, addHint: String, onAdd: (() -> Unit)? = null) {
    EditView(
        modifier = Modifier
            .clickable(
                enabled = isClickable,
                onClick = {
                    onAdd?.invoke()
                }
            )
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
                    modifier = Modifier
                        .clickable {
                            showAlertDialog.value = false
                            onDismiss()
                        }
                        .padding(horizontal = 5.dp)
                )
            },
            dismissButton = {
                Text(
                    "Keep Editing",
                    style = TextStyle(color = iosBlue, fontSize = 16.sp),
                    modifier = Modifier
                        .clickable {
                            showAlertDialog.value = false
                        }
                        .padding(horizontal = 5.dp)
                )
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