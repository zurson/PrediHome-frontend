package com.betoniarze.predihome.presentation.ui.prediction

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class PredictionRequestParameter(
    val name: String
)