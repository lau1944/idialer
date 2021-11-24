package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor

@Composable
fun StandardEditText(
    value: String? = "",
    hint: String? = "",
    backgroundColor: Color? = appColor().background,
    textStyle: TextStyle = LocalTextStyle.current,
    onChange: ((String) -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    TextField(
        value = value!!,
        onValueChange = { text ->
            onChange?.invoke(text)
        },
        textStyle = textStyle,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Blue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = backgroundColor!!
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone?.invoke()
            }
        ),
        singleLine = true,
        label = {
            Text(hint!!)
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}