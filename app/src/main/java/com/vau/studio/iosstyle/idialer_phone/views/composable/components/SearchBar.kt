package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray

@Composable
fun SearchBar(
    text: String? = "",
    hintText: String? = "",
    onChanged: (String?) -> Unit,
    onSearch: (String?) -> Unit
) {
    val textState = remember {
        mutableStateOf(TextFieldValue(text = text ?: ""))
    }
    TextField(
        value = textState.value,
        onValueChange = { value ->
            textState.value = value
            onChanged(value.text)
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Blue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = iosGray
        ),
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(textState.value.text)
            }
        ),
        singleLine = true,
        label = {
            Text(hintText ?: "Search")
        },
        leadingIcon = {
            SearchIcon()
        },
        trailingIcon = {
            CancelIcon {
                textState.value = TextFieldValue()
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CancelIcon(onCancel: () -> Unit) {
    Box(
        modifier = Modifier.clickable {
            onCancel()
        }
    ) {
        AssetImage(res = R.drawable.ic_cancel, size = 20, color = Color.Gray)
    }
}

@Composable
private fun SearchIcon() {
    AssetImage(res = R.drawable.ic_search, size = 20, color = Color.Gray)
}