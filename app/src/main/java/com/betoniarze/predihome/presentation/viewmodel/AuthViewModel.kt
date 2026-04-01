package com.betoniarze.predihome.presentation.viewmodel

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.betoniarze.predihome.core.host.MainActivity
import com.betoniarze.predihome.presentation.theme.Strings
import com.betoniarze.predihome.presentation.ui.form.GoogleAuthType
import com.betoniarze.predihome.utilities.FirebaseAuthManager
import com.betoniarze.predihome.utilities.changeActivity
import com.betoniarze.predihome.utilities.showToast


class AuthViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatedPassword by mutableStateOf("")

    var passwordVisible by mutableStateOf(false)
        private set

    val passwordVisualTransformation: VisualTransformation
        get() = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val visibilityIcon
        get() = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    val visibilityIconDescription: String
        get() = if (passwordVisible) Strings.LOGIN_HIDE_PASSWORD_ICON_DESCRIPTION else Strings.LOGIN_SHOW_PASSWORD_ICON_DESCRIPTION

    var isErrorEmail by mutableStateOf(false)
    var isErrorPassword by mutableStateOf(false)
    var isErrorRepeatPassword by mutableStateOf(false)


    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }


    fun validateEmail(): Boolean {
        isErrorEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return !isErrorEmail
    }


    fun validatePassword(): Boolean {
        isErrorPassword = password.length < 6
        return !isErrorPassword
    }


    fun validateRepeatPassword(): Boolean {
        isErrorRepeatPassword = password != repeatedPassword
        return !isErrorRepeatPassword
    }


    fun registerViaEmail(context: Context) {
        FirebaseAuthManager().registerViaEmail(email, password) { authStatus ->
            if (authStatus.success) {
                changeActivity(context = context, MainActivity::class, true)
            } else {
                showToast(context, authStatus.errorMessage, toastLength = Toast.LENGTH_LONG)
            }
        }
    }


    fun loginViaEmail(context: Context) {
        FirebaseAuthManager().loginViaEmail(email, password) { authStatus ->
            if (authStatus.success) {
                changeActivity(context = context, MainActivity::class, true)
            } else {
                showToast(context, authStatus.errorMessage, toastLength = Toast.LENGTH_LONG)
            }
        }
    }


    fun loginWithGoogle(context: Context, type: GoogleAuthType) {
        FirebaseAuthManager().openGoogleLoginMenu(context, type)
    }


    fun loginWithFacebook(context: Context) {
        FirebaseAuthManager().loginWithFacebook(context)
    }
}