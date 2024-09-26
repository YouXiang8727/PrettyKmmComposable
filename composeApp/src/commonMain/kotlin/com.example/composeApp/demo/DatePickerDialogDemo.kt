package com.example.composeApp.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youxiang8727.kmpsharedmodule.ui.DatePickerDialog
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DatePickerDialogDemo(
    currentDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    confirmAction: (LocalDate) -> Unit,
    dismissAction: () -> Unit
) {
    var selectedDate by remember {
        mutableStateOf(
            currentDate
        )
    }

    var showDate by remember {
        mutableStateOf(
            currentDate
        )
    }

    DatePickerDialog(
        initialDate = currentDate,
        title = {
            Text(
                text = "${selectedDate.dayOfWeek.name.substring(0, 3)}, " +
                        "${selectedDate.dayOfMonth} ${selectedDate.month.name.substring(0, 3)} " +
                        "${selectedDate.year}",
                style = MaterialTheme.typography.h6
            )
        },

        divider = {
            Divider(modifier = Modifier.fillMaxWidth())
        },

        subtitle = { minusMonthAction, plusMonthAction ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${showDate.year}${showDate.monthNumber.toString().padStart(2, '0')}"
                )

                IconButton(
                    onClick = {
                       minusMonthAction()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        plusMonthAction()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        },

        onSelectDateChange = {
            selectedDate = it
        },

        onShowDateChange = {
            showDate = it
        },

        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        dismissAction()
                    }
                ) {
                    Text("Cancel")
                }

                TextButton(
                    onClick = {
                        confirmAction(selectedDate)
                    }
                ) {
                    Text("OK")
                }
            }
        },

        modifier = Modifier.padding(16.dp)
    )
}