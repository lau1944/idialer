package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.DateUtil
import com.vau.studio.iosstyle.idialer_phone.core.TimeUtils
import com.vau.studio.iosstyle.idialer_phone.data.INCOMING_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.MISSED_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.OUTGOING_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.ContactPageType
import com.vau.studio.iosstyle.idialer_phone.data.models.INCOMING_CALL
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.backgroundGray
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactDetailViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel
import java.util.*

const val CONTACT_DETAIL_ROUTE = "contact_detail"
const val QUERY_PARAM_FIX = "?number={number}&prevName={prevName}&id={id}"

@Composable
fun ContactDetailUi(
    preName: String? = "",
    number: String,
    id: String? = "",
    mainViewModel: MainViewModel,
    contactViewModel: ContactDetailViewModel,
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
        val contactDetailState by contactViewModel.contactDetail.observeAsState()

        LaunchedEffect(true) {
            contactViewModel.initState()

            val page = ContactPageType.map(preName)
            if (page == ContactPageType.Recent) {
                contactViewModel.getCallLogByNumber(number)
            }
            if (page == ContactPageType.Contact) {
                contactViewModel.getContactDetailById(id!!)
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
                }
            })
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
        Date(contact.callDate!!.toLong())
    }

    InfoViewHolder {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                DateUtil.parseToDateFormat(callDate),
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(vertical = 7.dp)
            )

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
                .padding(15.dp),
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
private fun InfoViewHolder(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .shadow(5.dp)
            .background(appColor().background)
    ) {
        content()
    }
}