package com.betoniarze.predihome.core.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.emoji2.text.EmojiCompat
import com.betoniarze.predihome.R
import com.betoniarze.predihome.utilities.PERMISSIONS.REQUIRED_PERMISSIONS
import com.betoniarze.predihome.utilities.changeActivity
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()

        if (!allPermissionsGained())
            requestForPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebaseApp()
        initOthers()

        if (auth.currentUser == null) {
            changeActivity(this, LoginActivity::class, true)
        } else {
            changeActivity(this, AppActivity::class, true)
        }
    }

    private fun initializeFirebaseApp() {
        FirebaseApp.initializeApp(this)
        FacebookSdk.setApplicationId(getString(R.string.fb_app_id))
        FacebookSdk.setClientToken(getString(R.string.fb_app_secret))
        FacebookSdk.sdkInitialize(this)
    }

    private fun initOthers() {
        EmojiCompat.init(this)
    }

    private fun allPermissionsGained(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestForPermissions() =
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1)

}