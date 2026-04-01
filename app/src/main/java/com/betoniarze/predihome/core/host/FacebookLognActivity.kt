package com.betoniarze.predihome.core.host

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.betoniarze.predihome.R
import com.betoniarze.predihome.utilities.changeActivity
import com.betoniarze.predihome.utilities.showToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FacebookLoginActivity : Activity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private val context = this

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    showToast(context, getString(R.string.unexpected_error_message))
                }
            }
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    val exception = task.exception

                    if (exception is FirebaseAuthUserCollisionException) {
                        showToast(this, getString(R.string.auth_account_already_exists))
                    } else {
                        showToast(this, getString(R.string.unexpected_error_message))
                    }
                }

                updateUI()
            }
    }


    private fun updateUI() {
        changeActivity(this, MainActivity::class)
    }

}
