package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import android.provider.CallLog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.DateType
import com.vau.studio.iosstyle.idialer_phone.core.DateUtil
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel
import kotlin.math.abs

@ExperimentalMaterialApi
@Composable
fun CallLogItem(
    callHistory: CallHistory,
    onEdit: Boolean? = false,
    onDrag: Boolean? = false,
    callViewModel: CallViewModel,
    onDelete: ((CallHistory) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val maxCancelAreaWidth = 95f
        val cancelAreaWidth = remember { mutableStateOf(0f) }

        if (!onDrag!!) cancelAreaWidth.value = 0f

        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Divider(
                color = Color.LightGray.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 25.dp, vertical = 4.dp)
            )

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                callViewModel.changeCancelState(callHistory)

                                val x = dragAmount.x
                                cancelAreaWidth.value += -x
                            },
                            onDragEnd = {
                                if (cancelAreaWidth.value < maxCancelAreaWidth / 2) {
                                    cancelAreaWidth.value = 0f
                                } else {
                                    cancelAreaWidth.value = maxCancelAreaWidth
                                }
                            }
                        )
                    },
            ) {
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

                CancelArea(width = cancelAreaWidth.value, callHistory, onDelete)
            }
        }
    }
}

@Composable
private fun CancelArea(width: Float, callHistory: CallHistory, onDelete: ((CallHistory) -> Unit)?) {
    Box(
        modifier = Modifier
            .width(width.dp)
            .fillMaxHeight()
            .background(Color.Red)
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