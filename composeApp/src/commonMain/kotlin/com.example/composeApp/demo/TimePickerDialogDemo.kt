package com.example.composeApp.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.youxiang8727.kmpsharedmodule.ui.TimePickerDialog
import com.youxiang8727.kmpsharedmodule.ui.TimePickerDialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimePickerDialogDemo(
    currentTime: LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
    confirmAction: (LocalTime) -> Unit = {},
    dismissAction: () -> Unit = {}
) {
    var selectedTime by remember {
        mutableStateOf(currentTime)
    }

    TimePickerDialog(
        onDismissRequest = {
            dismissAction()
        },
        dialogProperties = DialogProperties(),
        timePickerDialogProperties = TimePickerDialogProperties(),
        initialTime = currentTime,
        topBar = {
            Text(
                text = "Time Picker",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(8.dp)
            )
        },

        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        dismissAction()
                    }
                ) {
                    Text(
                        text = "Cancel"
                    )
                }

                TextButton(
                    onClick = {
                        confirmAction(selectedTime)
                    }
                ) {
                    Text(
                        text = "OK"
                    )
                }
            }
        },
        onSelectedTimeChange = { time ->
            selectedTime = time
        }
    )
}