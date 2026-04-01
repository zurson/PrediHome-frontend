package com.betoniarze.predihome.core.host

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.ui.MainView

class AppActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            Theme.MainTheme {
                MainView()
            }
        }

    }

}