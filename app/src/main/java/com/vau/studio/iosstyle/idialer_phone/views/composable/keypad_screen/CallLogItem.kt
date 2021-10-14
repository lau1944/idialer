package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import android.provider.CallLog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.DateType
import com.vau.studio.iosstyle.idialer_phone.core.DateUtil
import com.vau.studio.iosstyle.idialer_phone.core.DeviceUtil
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SwipeableCard
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel

@ExperimentalMaterialApi
@Composable
fun CallLogItem(
    callHistory: CallHistory,
    isOnDeleteMode: Boolean? = false,
    onEdit: Boolean? = false,
    onTap: ((CallHistory) -> Unit)? = null,
    onDrag: ((CallHistory) -> Unit)? = null,
    onDelete: ((CallHistory) -> Unit)? = null
) {
    val maxCancelAreaWidth = 95f

    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .clickable {
                onTap?.invoke(callHistory)
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Divider(
                color = Color.LightGray.copy(alpha = 0.2f),
                thickness = 1.dp,
            )

            SwipeableCard(
                maxCancelAreaWidth = maxCancelAreaWidth,
                onDrag = { amount ->
                    onDrag?.invoke(callHistory)
                },
                endContent = {
                    CancelArea(callHistory = callHistory, onDelete = {
                        onDelete?.invoke(callHistory)
                    })
                }
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    if (onEdit!!) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .width(30.dp)
                                .fillMaxHeight()
                                .clickable {
                                    onDelete?.invoke(callHistory)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AssetImage(res = R.drawable.ic_minus, size = 20)
                        }
                    }

                    if (callHistory.type == CallLog.Calls.OUTGOING_TYPE) {
                        AssetImage(
                            res = R.drawable.ic_outgoing_call,
                            size = 20,
                            color = Color.LightGray,
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 7.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                            .padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        val textColor =
                            if (isMissedCall(callHistory.type!!)) Color.Red else appColor().surface
                        Text(
                            callHistory.name ?: callHistory.number ?: "",
                            style = TextStyle(color = textColor, fontSize = 18.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        PhoneLocationLayout(callHistory = callHistory)
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            handleCallDate(callHistory.date),
                            style = TextStyle(color = Color.LightGray, fontSize = 15.sp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        AssetImage(res = R.drawable.ic_info, size = 22, color = iosBlue)
                    }
                }
            }

        }
    }
}

@Composable
private fun CancelArea(callHistory: CallHistory, onDelete: ((CallHistory) -> Unit)?) {
    Box(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable {
                onDelete?.invoke(callHistory)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Delete",
            style = TextStyle(color = Color.White),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PhoneLocationLayout(callHistory: CallHistory) {
    Row {
        Text(
            "P",
            style = TextStyle(color = Color.LightGray, fontSize = 16.sp),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        if (!callHistory.location.isNullOrEmpty()) {
            Text(callHistory.location, style = TextStyle(color = Color.LightGray, fontSize = 15.sp))
        }
        Text("mobile", style = TextStyle(color = Color.LightGray, fontSize = 15.sp))
    }
}

private fun handleCallDate(dateStr: String?): String {
    if (dateStr == null) return ""

    val date = DateUtil.parseToDate(dateStr.toLong())
    if (date != null) {
        val dayDif = DateUtil.compareDateDif(date, DateType.Day)
        if (dayDif < 1) {
            return DateUtil.parseToTimeFormat(date)
        } else if (dayDif == 1) {
            return "Yesterday"
        }
    }
    return DateUtil.parseToDateFormat(date)
}

private fun isMissedCall(type: Int): Boolean {
    return CallLog.Calls.MISSED_TYPE == type
}