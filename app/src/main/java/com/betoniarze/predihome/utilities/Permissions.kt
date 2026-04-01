package com.betoniarze.predihome.utilities

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat

object PERMISSIONS {
    internal val REQUIRED_PERMISSIONS = listOf (
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    ).toTypedArray()
}

fun isPermissionsGainded(context: Context): Boolean {
    return PERMISSIONS.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}