package com.betoniarze.predihome.utilities

import android.content.Context
import android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL
import android.util.Log
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betoniarze.predihome.R
import com.betoniarze.predihome.core.host.FacebookLoginActivity
import com.betoniarze.predihome.core.host.MainActivity
import com.betoniarze.predihome.presentation.ui.form.GoogleAuthType
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)


    private suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).await()
    }


    private suspend fun linkAccountWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.currentUser!!.linkWithCredential(firebaseCredential).await()
    }


    fun loginViaEmail(email: String, password: String, callback: (AuthStatus) -> Unit) {
        scope.launch {
            val result = loginInternal(email, password)
            callback(result)
        }
    }


    fun registerViaEmail(email: String, password: String, callback: (AuthStatus) -> Unit) {
        scope.launch {
            val result = registerInternal(email, password)
            callback(result)
        }
    }


    private suspend fun loginInternal(email: String, password: String): AuthStatus {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            AuthStatus(success = true, errorMessage = null)
        } catch (e: Exception) {
            AuthStatus(success = false, errorMessage = e.message)
        }
    }


    private suspend fun registerInternal(email: String, password: String): AuthStatus {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()

            AuthStatus(success = true, errorMessage = null)
        } catch (e: Exception) {
            AuthStatus(success = false, errorMessage = e.message)
        }
    }


    suspend fun getToken(): String =
        auth.currentUser?.getIdToken(false)?.await()?.token ?: ""


    private fun printToken() = scope.launch {
        val token = getToken()
        Log.i("JWT TOKEN", token)
    }


    fun openGoogleLoginMenu(context: Context, authType: GoogleAuthType) {
        val credentialManager = CredentialManager.create(context = context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                when (authType) {
                    GoogleAuthType.LOGIN -> onSignInWithGoogle(context, result.credential)
                    GoogleAuthType.REGISTER -> onSignUpWithGoogle(context, result.credential)
                }

            } catch (e: Exception) {
                when (e) {
                    is GetCredentialCancellationException -> return@launch

                    is GetCredentialException -> {
                        if (e.type == TYPE_NO_CREDENTIAL) {
                            showToast(
                                context,
                                text = context.getString(R.string.auth_no_google_account_found)
                            )
                            return@launch
                        }
                    }
                }

                e.printStackTrace()
                showToast(
                    context = context,
                    text = context.getString(R.string.unexpected_error_message),
                    toastLength = Toast.LENGTH_LONG
                )
            }
        }
    }


    private fun onSignInWithGoogle(context: Context, credential: Credential) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                signInWithGoogle(googleIdTokenCredential.idToken)
                changeActivity(context, MainActivity::class, true)
            } else {
                showToast(
                    context = context,
                    text = context.getString(R.string.unexpected_error_message),
                    toastLength = Toast.LENGTH_LONG
                )
            }
        }
    }


    private fun onSignUpWithGoogle(context: Context, credential: Credential) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                linkAccountWithGoogle(googleIdTokenCredential.idToken)
                changeActivity(context, MainActivity::class, true)
            } else {
                showToast(
                    context = context,
                    text = context.getString(R.string.unexpected_error_message),
                    toastLength = Toast.LENGTH_LONG
                )
            }
        }
    }


    fun loginWithFacebook(context: Context) {
        changeActivity(context, FacebookLoginActivity::class)
    }


    fun getEmail() = auth.currentUser?.email


    fun logoutUser(context: Context) {
        auth.signOut()
        changeActivity(context = context, activity = MainActivity::class, finish = true)
    }

}
