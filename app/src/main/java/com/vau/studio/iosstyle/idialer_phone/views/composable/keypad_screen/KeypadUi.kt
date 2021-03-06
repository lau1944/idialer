package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.data.models.KeypadNumber
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.ContactModifyView
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactDetailViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.DialerViewModel

@ExperimentalComposeUiApi
@Composable
fun DialerScreen(
    dialerViewModel: DialerViewModel = viewModel(),
    contactDetailViewModel: ContactDetailViewModel
) {
    val numberList = dialerViewModel.inputNumber.observeAsState()
    val context = LocalContext.current
    val collection = remember { getKeypadCollections() }

    var showAddNumberDialer by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(bottom = 20.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DialerNumberScreen(numberList.value, onAddNumber = {
            showAddNumberDialer = true
        })
        KeypadLayout(
            collection = collection,
            hasNumber = !numberList.value.isNullOrEmpty(),
            onCall = {
                OpenUtil.openPhoneApp(context, dialerViewModel.numberListToString())
            },
            onTap = { number ->
                dialerViewModel.appendNumber(number = number)
            },
            onRemoved = {
                dialerViewModel.removeNumber()
            })
    }

    if (showAddNumberDialer) {
        ContactModifyView(
            initNumber = dialerViewModel.numberListToString(),
            contactDetailViewModel = contactDetailViewModel,
            onCancel = { showAddNumberDialer = false }) {
            showAddNumberDialer = false
        }
    }

}

@Composable
fun DialerNumberScreen(number: List<String>?, onAddNumber: () -> Unit) {
    val buffer = StringBuffer()
    if (!number.isNullOrEmpty()) {
        for (num in number) {
            buffer.append(num)
        }
    }
    val currentNumber = buffer.toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            currentNumber,
            modifier = Modifier.padding(horizontal = 15.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 24.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (currentNumber.isNotEmpty()) {
            Text(
                "Add number",
                style = TextStyle(color = iosBlue, fontSize = 16.sp),
                modifier = Modifier.clickable { onAddNumber() })
        }
    }
}

private fun getKeypadCollections(): List<List<KeypadNumber>> {
    val keypadList = arrayListOf<List<KeypadNumber>>()
    keypadList.add(
        listOf(
            KeypadNumber("1"),
            KeypadNumber("2", listOf("A", "B", "C")),
            KeypadNumber("3", listOf("D", "E", "F"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("4", listOf("G", "H", "I")),
            KeypadNumber("5", listOf("J", "K", "L")),
            KeypadNumber("6", listOf("M", "N", "O"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("7", listOf("P", "Q", "R", "S")),
            KeypadNumber("8", listOf("T", "U", "V")),
            KeypadNumber("9", listOf("W", "X", "Y", "Z"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("*"),
            KeypadNumber("0", listOf("+")),
            KeypadNumber("#")
        )
    )
    return keypadList
}