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
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue

@Composable
fun CallLogItem(callHistory: CallHistory, onDelete: ((CallHistory) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .padding(7.dp)
            .height(50.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                if (callHistory.type == CallLog.Calls.OUTGOING_TYPE) {
                    AssetImage(
                        res = R.drawable.ic_outgoing_call,
                        size = 20,
                        color = Color.LightGray
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        callHistory.name!!,
                        style = TextStyle(color = appColor().surface, fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!callHistory.location.isNullOrEmpty()) {
                        Text(
                            callHistory.location,
                            style = TextStyle(color = Color.LightGray, fontSize = 15.sp)
                        )
                    }
                }

                Box(
                    modifier = Modifier.padding(horizontal = 7.dp)
                ) {
                    Text(
                        callHistory.date ?: "",
                        style = TextStyle(color = Color.LightGray, fontSize = 15.sp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    AssetImage(res = R.drawable.ic_info, size = 25, color = iosBlue)
                }
            }
        }
    }
}