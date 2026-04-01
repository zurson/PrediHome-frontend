package com.betoniarze.predihome.presentation.ui.prediction

import androidx.compose.runtime.MutableState
import com.betoniarze.predihome.utilities.Units

data class SliderData (
    val label: String,
    var value: MutableState<Float>,
    val unit: Units,
    val onValueChange: (Float) -> Unit,
    val valueRange: ClosedFloatingPointRange<Float>,
    val moreSteps: Boolean = false,
    val nonDecimal: Boolean = false,
) {
    fun steps(): Int {
        return if (moreSteps) 100 else 0
    }
}