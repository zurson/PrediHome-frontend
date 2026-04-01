package com.betoniarze.predihome.presentation.ui.prediction

import com.betoniarze.predihome.utilities.Units

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class PredictionResultViewParameter(
    val name: String,
    val unit: Units,
    val weight: Int
)