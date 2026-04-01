package com.betoniarze.predihome.core.host

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.ui.form.LoginView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme.MainTheme {
                LoginView()
            }
        }

    }

}