package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen

import android.content.Intent
import android.os.Build
import android.telephony.emergency.EmergencyNumber
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vau.studio.iosstyle.idialer_phone.BuildConfig
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.DateUtil
import com.vau.studio.iosstyle.idialer_phone.core.ShareHandler
import com.vau.studio.iosstyle.idialer_phone.core.TimeUtils
import com.vau.studio.iosstyle.idialer_phone.data.INCOMING_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.MISSED_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.OUTGOING_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.ContactPageType
import com.vau.studio.iosstyle.idialer_phone.data.models.INCOMING_CALL
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.*
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.ContactAddView
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactDetailViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel
import java.util.*

const val CONTACT_DETAIL_ROUTE = "contact_detail"
const val QUERY_PARAM_FIX = "?number={number}&prevName={prevName}&id={id}"

@ExperimentalComposeUiApi
@Composable
fun ContactDetailUi(
    preName: String? = "",
    number: String,
    id: String? = "",
    mainViewModel: MainViewModel,
    contactViewModel: ContactViewModel,
    contactDetailViewModel: ContactDetailViewModel,
) {
    val backgroundColor = remember {
        backgroundGray
    }

    Scaffold(
        topBar = {
            DetailAppbar(prevName = preName!!, backgroundColor = backgroundColor) {
                mainViewModel.popBack()
            }
        },
        backgroundColor = backgroundColor
    ) {
        val contactDetailState by contactDetailViewModel.contactDetail.observeAsState()
        val isDefaultDialer by mainViewModel.isDefaultCaller.observeAsState(false)
        val context = LocalContext.current
        val showDialog = remember {
            mutableStateOf(false)
        }

        if (showDialog.value) {
            ShowContactCreateDialog(contactDetailViewModel = contactDetailViewModel, onDone = {

            }, onDismiss = {
                showDialog.value = false
            })
        }

        LaunchedEffect(true) {
            contactDetailViewModel.initState()

            val page = ContactPageType.map(preName)
            if (page == ContactPageType.Recent) {
                contactDetailViewModel.getCallLogByNumber(number)
            }
            if (page == ContactPageType.Contact) {
                contactDetailViewModel.getContactDetailById(id!!)
            }
        }

        UiProgressLayout(state = contactDetailState) {
            val contact: Contact = (contactDetailState as UiState.Success).data as Contact

            LazyColumn(content = {
                item {
                    UserInfoView(contact = contact)
                    UserActionView(contact = contact)

                    if (preName == "Recents") {
                        Box(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            CallInfoView(contact = contact)
                        }
                    }
                    ContactEditView(
                        contact = contact,
                        contactViewModel = contactViewModel,
                        onShare = {
                            ShareHandler.shareText(context, contact.number.toString())
                        },
                        onCreate = {
                            showDialog.value = true
                        })

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isDefaultDialer)
                        BlockContactView(number = contact.number.toString(), contactViewModel)
                }
            })
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun ShowContactCreateDialog(
    contactDetailViewModel: ContactDetailViewModel,
    onDismiss: () -> Unit,
    onDone: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ContactAddView(
            contactDetailViewModel = contactDetailViewModel,
            onCancel = { onDismiss() }) {
            onDone()
        }
    }
}

/**
 * User info header view
 */
@Composable
private fun UserInfoView(contact: Contact) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AssetImage(res = R.drawable.ic_big_user, size = 65)
        Text(
            contact.name ?: contact.number?.toString() ?: "Unknown",
            style = TextStyle(color = appColor().surface, fontSize = 24.sp),
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

/**
 * Action row view
 */
@Composable
private fun UserActionView(contact: Contact) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        if (null != contact.name) {
            ActionViewBox(res = R.drawable.ic_full_message, text = "Message")
        }

        if (null != contact.number) {
            ActionViewBox(res = R.drawable.ic_phone, text = "Phone")
        }

        if (null != contact.email) {
            ActionViewBox(res = R.drawable.ic_letter, text = "Mail")
        }
    }
}

/**
 * Call Info View (only if it's from recent call)
 */
@Composable
private fun CallInfoView(contact: Contact) {
    val callDate = remember {
        if (contact.callDate.isNullOrBlank()) null else Date(contact.callDate.toLong())
    }

    InfoViewHolder {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            if (callDate != null) {
                Text(
                    DateUtil.parseToDateFormat(callDate),
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(vertical = 7.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    DateUtil.parseToTimeFormat(callDate),
                    style = TextStyle(fontSize = 12.sp),
                )

                when (contact.type) {
                    MISSED_CALL_TYPE -> Text(
                        "Missed call",
                        style = TextStyle(color = appColor().surface, fontWeight = FontWeight.Bold)
                    )

                    INCOMING_CALL_TYPE -> Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                "Incomming Call",
                                style = TextStyle(
                                    color = appColor().surface,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (contact.duration != null && contact.duration != "0") {
                                Text(
                                    TimeUtils.parseToString(contact.duration.toLongOrNull()),
                                    style = TextStyle(
                                        fontSize = 12.sp, color = appColor().surface.copy(0.5f)
                                    ),
                                )
                            }
                        }
                    }

                    OUTGOING_CALL_TYPE -> Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            "Outgoing Call",
                            style = TextStyle(
                                color = appColor().surface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        if (contact.duration != null && contact.duration != "0") {
                            Text(
                                TimeUtils.parseToString(contact.duration.toLongOrNull()),
                                style = TextStyle(
                                    fontSize = 12.sp, color = appColor().surface.copy(0.5f)
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun BlockContactView(number: String, contactViewModel: ContactViewModel) {
    //val blockedNumbers by contactViewModel.blockNumbers.observeAsState()
    val isInBlock = contactViewModel.isInBlock(number)

    Box(modifier = Modifier.padding(vertical = 15.dp)) {
        InfoViewHolder {
            Text(
                if (isInBlock) "Unblock this user" else "Block this Caller", style = TextStyle(
                    color = iosRed,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .clickable {
                        if (isInBlock) {
                            contactViewModel.removeBlockNumber(number)
                        } else {
                            contactViewModel.addBlockNumber(number)
                        }
                    }
            )
        }
    }
}

/**
 * Contact edit
 */
@Composable
private fun ContactEditView(
    contact: Contact,
    contactViewModel: ContactViewModel,
    onShare: () -> Unit,
    onCreate: () -> Unit,
) {
    InfoViewHolder(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onShare.invoke()
                    }
            ) {
                Text(
                    "Share Contact", style = TextStyle(
                        color = iosBlue,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(15.dp)
                )
            }

            if (!contactViewModel.existInContact(contact.name)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCreate()
                    }) {
                    Divider(
                        modifier = Modifier
                            .background(iosGray.copy(alpha = 0.1f))
                    )
                    Text(
                        "Create New Contact", style = TextStyle(
                            color = iosBlue,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionViewBox(onTap: (() -> Unit)? = null, res: Int, text: String) {
    InfoViewHolder {
        Box(
            modifier = Modifier
                .width(85.dp)
                .background(appColor().background)
                .clickable {
                    onTap?.invoke()
                }
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AssetImage(res = res, size = 25, color = iosBlue)
                Text(text, style = TextStyle(color = iosBlue, fontSize = 12.sp))
            }
        }
    }
}

@Composable
private fun InfoViewHolder(modifier: Modifier? = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier!!
            .clip(RoundedCornerShape(16.dp))
            .shadow(5.dp)
            .background(appColor().background)
            .padding(horizontal = 10.dp)
    ) {
        content()
    }
}