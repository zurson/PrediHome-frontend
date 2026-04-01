package com.betoniarze.predihome.presentation.ui.lists.history

import com.google.firebase.Timestamp
import java.util.Locale

data class HistoryRecord(
    val uid: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    val predictionDate: Timestamp? = null,
    val price: Int = 0,
    val city: String = "",
    val squareMeters: Double = 0.0,
    val centreDistance: Double = 0.0,
    val floorCount: Int = 0,
    val rooms: Int = 0,

    val kindergartenDistance: Double = 0.0,
    val restaurantDistance: Double = 0.0,
    val collegeDistance: Double = 0.0,
    val postOfficeDistance: Double = 0.0,
    val clinicDistance: Double = 0.0,
    val schoolDistance: Double = 0.0,
    val pharmacyDistance: Double = 0.0,
    val poiCount: Int = 0
) {

    val formattedCity: String
        get() {
            return if (city.isNotEmpty()) {
                city.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            } else {
                "Unknown City"
            }
        }
}