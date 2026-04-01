package com.betoniarze.predihome.core.communication

import com.betoniarze.predihome.utilities.replacePolishCharacters
import com.google.gson.Gson

class PredictionRequest(
    private val properties: MutableMap<String, Any?> = mutableMapOf()
) {

    companion object {
        val ALLOWED_KEYS = setOf(
            "city", "squareMeters", "longitude", "latitude", "centreDistance",
            "floorCount", "rooms", "kindergartenDistance", "restaurantDistance",
            "collegeDistance", "postOfficeDistance", "clinicDistance",
            "schoolDistance", "pharmacyDistance", "poiCount"
        )
    }


    init {
        ALLOWED_KEYS.forEach { key ->
            setProperty(key, 0)
        }

    }


    fun setProperty(key: String, value: Any?): PredictionRequest {
        if (key in ALLOWED_KEYS) {
            val processedValue = if (key == "city" && value is String) {
                replacePolishCharacters(value)
            } else {
                value
            }
            properties[key] = processedValue
        } else {
            throw IllegalArgumentException("Key '$key' is not allowed in PredictionRequest")
        }
        return this
    }



    @Suppress("UNCHECKED_CAST")
    fun <T> getProperty(key: String): T? {
        return properties[key] as? T
    }


    fun removeProperty(key: String): PredictionRequest {
        if (key in ALLOWED_KEYS) {
            properties.remove(key)
        } else {
            throw IllegalArgumentException("Key '$key' is not allowed in PredictionRequest")
        }
        return this
    }


    fun getAllProperties(): Map<String, Any?> {
        return properties.toMap()
    }


    fun toJson(): String {
        return Gson().toJson(properties)
    }
}
