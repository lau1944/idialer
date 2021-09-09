package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import android.provider.CallLog
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.util.*

@Composable
fun CallLogItem(callHistory: CallHistory, onDelete: ((CallHistory) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
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
                    .fillMaxSize(),
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
            }
        }
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