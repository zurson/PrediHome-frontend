package com.betoniarze.predihome.presentation.ui.prediction

import com.betoniarze.predihome.utilities.Units

data class PredictionParameter(
    val name: String,
    val value: Any,
    val unit: Units
)