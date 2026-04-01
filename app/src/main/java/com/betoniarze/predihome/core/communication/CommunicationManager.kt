package com.betoniarze.predihome.core.communication

import android.util.Log
import com.betoniarze.predihome.presentation.theme.Strings
import com.betoniarze.predihome.utilities.FirebaseAuthManager
import com.betoniarze.predihome.presentation.ui.lists.history.HistoryRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class CommunicationManager {

    suspend fun fetchPopularRegions(): Map<String, Long>? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://predihome-backend.onrender.com/api/statistics")

                val connection = url.openConnection() as HttpURLConnection
                val token = getAuthToken()

                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", "application/json")

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val json = inputStream.bufferedReader().use { it.readText() }
                    //Log.i("PREGIONS_JSON", json)

                    val mapType = object : TypeToken<Map<String, Long>>() {}.type
                    val rawMap: Map<String, Long> = Gson().fromJson(json, mapType)

                    val result = rawMap.entries
                        .sortedByDescending { it.value }
                        .associate { entry -> entry.key.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        } to entry.value }

                    //Log.i("PREGIONS_RESULT", result.toString())

                    inputStream.close()
                    result
                } else {
                    Log.e(TAG, "(POPULAR_REGIONS) Response code: $responseCode. Message: ${connection.responseMessage}")

                    connection.errorStream?.bufferedReader()?.use {
                        Log.e(TAG, "(POPULAR_REGIONS) Error message: ${it.readText()}")
                    }
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun fetchHistoryRecords(page: Int, pageSize: Int): List<HistoryRecord>? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://predihome-backend.onrender.com/api/history?page=$page&pageSize=$pageSize")

                val connection = url.openConnection() as HttpURLConnection
                val token = getAuthToken()

                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", "application/json")

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { reader ->
                        val responseJson = reader.readText()
                        val type = object : TypeToken<List<HistoryRecord>>() {}.type

                        Gson().fromJson<List<HistoryRecord>>(responseJson, type)
                    }
                } else {
                    Log.e(TAG, "(HISTORY) Response code: $responseCode. Message: ${connection.responseMessage}")

                    connection.errorStream?.bufferedReader()?.use {
                        Log.e(TAG, "(HISTORY) Error message: ${it.readText()}")
                    }
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun sendPredictionRequest(predictionRequest: PredictionRequest): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(Strings.COMMUNICATION_PREDICTION_REQUEST_URL)
                val token = getAuthToken()

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBytes = predictionRequest.toJson().toByteArray(Charsets.UTF_8)
                connection.setRequestProperty("Content-Length", jsonBytes.size.toString())

                connection.outputStream.use { outputStream ->
                    outputStream.write(jsonBytes)
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText().toInt() }
                } else {
                    Log.e(
                        TAG,
                        "Response code: $responseCode. Message: ${connection.responseMessage}"
                    )

                    connection.errorStream?.bufferedReader()?.use {
                        Log.e(TAG, "Error message: ${it.readText()}")
                    }
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    private suspend fun getAuthToken() = FirebaseAuthManager().getToken()

    companion object {
        const val TAG = "COMMUNICATION MANAGER"
    }
}


