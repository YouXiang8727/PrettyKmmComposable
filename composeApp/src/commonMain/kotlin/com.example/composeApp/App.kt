package com.example.composeApp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.composeApp.demo.DatePickerDialogDemo
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showDatePickerDialog by remember {
            mutableStateOf(false)
        }

        var currentDate by remember {
            mutableStateOf(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                OutlinedTextField(
                    onValueChange = {

                    },
                    value = currentDate.format(LocalDate.Formats.ISO_BASIC),
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.clickable {
                        showDatePickerDialog = true
                    }
                )
            }

            if (showDatePickerDialog) {
                DatePickerDialogDemo(
                    currentDate = currentDate,
                    confirmAction = {
                        currentDate = it
                        showDatePickerDialog = false
                    },
                    dismissAction = {
                        showDatePickerDialog = false
                    }
                )
            }
        }
    }
}