package com.youxiang8727.kmpsharedmodule.ui

import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color

data class TimePickerDialogProperties(
    @IntRange(from = 3, to = 7)
    val visibleContentSize: Int = 5,
    val contentAspectRatio: Float = 1.5f,
    val maskColor: Color = Color.White,
    val selectedLineColor: Color = Color.LightGray,
    val selectedLineWidth: Float = 1f
) {
    val hours: List<Int> = (0..23).toList()

    val minutes: List<Int> = (0..59).toList()

    val autoAdjustColumnAspectRatio = contentAspectRatio / 2
    val itemAspectRatio: Float = autoAdjustColumnAspectRatio * visibleContentSize
}
