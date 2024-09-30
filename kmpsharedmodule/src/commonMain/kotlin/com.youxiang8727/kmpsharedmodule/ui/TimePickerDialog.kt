package com.youxiang8727.kmpsharedmodule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit = {},
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false
    ),
    timePickerDialogProperties: TimePickerDialogProperties = TimePickerDialogProperties(),
    initialTime: LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
    topBar: @Composable (RowScope.() -> Unit),
    bottomBar: @Composable (RowScope.() -> Unit),
    onSelectedTimeChange: (LocalTime) -> Unit = {}
) {
    var time by remember {
        mutableStateOf(initialTime)
    }

    LaunchedEffect(time) {
        onSelectedTimeChange(time)
    }

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = dialogProperties
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    topBar()
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(timePickerDialogProperties.contentAspectRatio)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        timePickerDialogProperties.maskColor,
                                        Color.Transparent,
                                        timePickerDialogProperties.maskColor
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                )
                            )

                            val halfItemHeight = size.height / timePickerDialogProperties.visibleContentSize.toFloat() / 2f
                            val center = size.height / 2f
                            drawLine(
                                color = timePickerDialogProperties.selectedLineColor,
                                strokeWidth = timePickerDialogProperties.selectedLineWidth,
                                start = Offset(0f, center - halfItemHeight),
                                end = Offset(size.width, center - halfItemHeight)
                            )

                            drawLine(
                                color = timePickerDialogProperties.selectedLineColor,
                                strokeWidth = timePickerDialogProperties.selectedLineWidth,
                                start = Offset(0f, center + halfItemHeight),
                                end = Offset(size.width, center + halfItemHeight)
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoAdjustColumn<Int>(
                        data = timePickerDialogProperties.hours,
                        initialData = initialTime.hour,
                        onDataChangeAction = { hour ->
                            time = LocalTime(hour, time.minute)
                        },
                        visibleContentSize = timePickerDialogProperties.visibleContentSize,
                        modifier = Modifier.weight(1f)
                            .fillMaxWidth()
                            .aspectRatio(timePickerDialogProperties.autoAdjustColumnAspectRatio)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(timePickerDialogProperties.itemAspectRatio),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.toString().padStart(2, '0')
                            )
                        }
                    }

                    Text(
                        text = ":"
                    )

                    AutoAdjustColumn<Int>(
                        data = timePickerDialogProperties.minutes,
                        initialData = initialTime.minute,
                        onDataChangeAction = { minute ->
                            time = LocalTime(time.hour, minute)
                        },
                        visibleContentSize = timePickerDialogProperties.visibleContentSize,
                        modifier = Modifier.weight(1f)
                            .fillMaxWidth()
                            .aspectRatio(timePickerDialogProperties.autoAdjustColumnAspectRatio)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(timePickerDialogProperties.itemAspectRatio),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.toString().padStart(2, '0')
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottomBar()
                }
            }
        }
    }
}