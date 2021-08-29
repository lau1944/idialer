package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.core.ColorUtils
import com.vau.studio.iosstyle.idialer_phone.data.models.KeypadNumber

@Composable
fun NumberCircleButton(keypadNumber: KeypadNumber, onTap: (String) -> Unit) {
    Button(
        onClick = {
            onTap(keypadNumber.number)
        },
        elevation = noElevation(),
        colors = ButtonDefaults.buttonColors(backgroundColor = ColorUtils.parseColor("#eeeeee")),
        shape = CircleShape,
        modifier = Modifier
            .size(keyButtonSize().dp)
            .padding(6.dp),
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                keypadNumber.number,
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
            )

            Row {
                for (text in keypadNumber.optionalString!!) {
                    Text(
                        text,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        ),
                    )
                }
            }

        }
    }
}