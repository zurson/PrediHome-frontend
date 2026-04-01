package com.betoniarze.predihome.utilities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {
    object RegisterView : Screen()
    object LoginView : Screen()
}

object AppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.LoginView)

    fun navigateTo(dest: Screen) {
        currentScreen.value = dest
    }

}