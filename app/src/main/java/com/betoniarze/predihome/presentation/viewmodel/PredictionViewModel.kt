package com.betoniarze.predihome.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.core.communication.CommunicationManager
import com.betoniarze.predihome.core.communication.PredictionRequest
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.ui.prediction.PredictionParameter
import com.betoniarze.predihome.presentation.ui.prediction.PredictionRequestParameter
import com.betoniarze.predihome.presentation.ui.prediction.PredictionResultViewParameter
import com.betoniarze.predihome.presentation.ui.prediction.SliderData
import com.betoniarze.predihome.presentation.ui.prediction.map.City
import com.betoniarze.predihome.utilities.Units
import com.betoniarze.predihome.utilities.roundToFourPlace
import com.betoniarze.predihome.utilities.roundToOnePlace
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

class PredictionViewModel : ViewModel() {
    var mapsData by mutableStateOf(City())
        private set

    private val _predictionInProgress = mutableStateOf(false)
    val predictionInProgress: Boolean get() = _predictionInProgress.value

    var predictionValue: Int? = 0

    @PredictionRequestParameter("city")
    @PredictionResultViewParameter("Region", Units.NONE, 100)
    private val city: String
        get() = mapsData.name

    @PredictionRequestParameter("squareMeters")
    @PredictionResultViewParameter("Area", Units.SQUARE_METERS, 99)
    private var squareMeters = mutableFloatStateOf(25f)

    @PredictionRequestParameter("floorCount")
    @PredictionResultViewParameter("Floors", Units.NONE, 98)
    private var floorCount = mutableFloatStateOf(1f)

    @PredictionRequestParameter("rooms")
    @PredictionResultViewParameter("Rooms", Units.NONE, 97)
    private var rooms = mutableFloatStateOf(1f)

    @PredictionRequestParameter("centreDistance")
    @PredictionResultViewParameter("City Center", Units.KILOMETERS, 50)
    private val centreDistance: Float
        get() = fixValue(mapsData.centreDistance, false)

    @PredictionRequestParameter("kindergartenDistance")
    @PredictionResultViewParameter("Kindergarten", Units.KILOMETERS, 50)
    private var kindergartenDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("restaurantDistance")
    @PredictionResultViewParameter("Restaurant", Units.KILOMETERS, 49)
    private var restaurantDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("collegeDistance")
    @PredictionResultViewParameter("College", Units.KILOMETERS, 48)
    private var collegeDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("postOfficeDistance")
    @PredictionResultViewParameter("Post office", Units.KILOMETERS, 47)
    private var postOfficeDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("clinicDistance")
    @PredictionResultViewParameter("Clinic", Units.KILOMETERS, 46)
    private var clinicDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("schoolDistance")
    @PredictionResultViewParameter("School", Units.KILOMETERS, 45)
    private var schoolDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("pharmacyDistance")
    @PredictionResultViewParameter("Pharmacy", Units.KILOMETERS, 44)
    private var pharmacyDistance = mutableFloatStateOf(0f)

    @PredictionRequestParameter("latitude")
    @PredictionResultViewParameter("Latitude", Units.NONE, 43)
    private val latitude: Double
        get() = roundToFourPlace(mapsData.latlng.latitude)

    @PredictionRequestParameter("longitude")
    @PredictionResultViewParameter("Longitude", Units.NONE, 42)
    private val longitude: Double
        get() = roundToFourPlace(mapsData.latlng.longitude)

    @PredictionRequestParameter("poiCount")
    private var poiCount = mutableFloatStateOf(0f)


    fun updateMapsData(city: City) {
        mapsData = city
    }


    private fun predict(viewModel: PredictionViewModel) {
        val predictionRequest = PredictionRequest()

        val properties = PredictionViewModel::class.declaredMemberProperties.filter {
            it.findAnnotation<PredictionRequestParameter>() != null
        }

        for (property in properties) {
            property.isAccessible = true

            val annotation = property.findAnnotation<PredictionRequestParameter>()!!
            val name = annotation.name

            val value = when (val propertyValue = property.get(viewModel)) {
                is MutableState<*> -> propertyValue.value
                else -> propertyValue
            }

            predictionRequest.setProperty(
                key = name,
                value = value
            )
        }

        println(predictionRequest.toJson())
        viewModelScope.launch {
            _predictionInProgress.value = true
            predictionValue = CommunicationManager().sendPredictionRequest(predictionRequest)
            _predictionInProgress.value = false
        }
    }


    fun navigateTo(
        navController: NavController,
        navigationItem: NavigationItem,
        doPredictionBefore: Boolean = false
    ) {
        if (doPredictionBefore)
            predict(this)

        navController.navigate(navigationItem.route)
    }


    fun getResultValueWithUnit(value: Any, unit: Units): String {
        val resultValue = when (value) {
            is Float -> if (value % 1 == 0f) value.toInt() else value
            is Double -> if (value % 1.0 == 0.0) value.toInt() else value
            is Number -> value
            else -> value
        }

        return if (unit != Units.NONE) "$resultValue $unit" else resultValue.toString()
    }


    fun getResultValueWithUnit(value: Float, unit: Units): String {
        val resultValue = if (value % 1 == 0f) value.toInt() else value
        return if (unit != Units.NONE) "$resultValue $unit" else resultValue.toString()
    }


    fun fixValue(value: Float, nonDecimal: Boolean): Float {
        return if (nonDecimal) value.roundToInt().toFloat() else roundToOnePlace(value)
    }


    fun fixValue(value: Double, nonDecimal: Boolean): Float {
        return if (nonDecimal) value.roundToInt().toFloat() else roundToOnePlace(value)
    }


    @Composable
    fun getParametersList(viewModel: PredictionViewModel): List<PredictionParameter> {
        val parameters = mutableListOf<PredictionParameter>()
        val properties = PredictionViewModel::class.declaredMemberProperties

        val fixedProperties =
            properties.filter { it.findAnnotation<PredictionResultViewParameter>() != null }
        val sortedProperties =
            fixedProperties.sortedByDescending {
                it.findAnnotation<PredictionResultViewParameter>()?.weight ?: 0
            }

        for (property in sortedProperties) {
            property.isAccessible = true

            val annotation = property.findAnnotation<PredictionResultViewParameter>()!!
            val name = annotation.name
            val unit = annotation.unit

            val value = when (val propertyValue = property.get(viewModel)) {
                is MutableState<*> -> propertyValue.value
                else -> propertyValue
            } ?: stringResource(R.string.prediction_parameter_value_error)

            parameters.add(PredictionParameter(name, value, unit))
        }

        return parameters
    }


    val sliderDataList = mutableStateListOf(
        SliderData(label = "Area",
            value = squareMeters,
            unit = Units.SQUARE_METERS,
            nonDecimal = true,
            valueRange = 25f..150f,
            onValueChange = { squareMeters.floatValue = it }
        ),

        SliderData(label = "Floors",
            value = floorCount,
            unit = Units.NONE,
            nonDecimal = true,
            valueRange = 1f..29f,
            onValueChange = { floorCount.floatValue = it }),

        SliderData(label = "Rooms",
            value = rooms,
            unit = Units.NONE,
            nonDecimal = true,
            valueRange = 1f..6f,
            onValueChange = { rooms.floatValue = it }),

        SliderData(label = "Kindergarten distance",
            value = kindergartenDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0.0f..5f,
            onValueChange = { kindergartenDistance.floatValue = it }),

        SliderData(label = "Restaurant distance",
            value = restaurantDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { restaurantDistance.floatValue = it }),

        SliderData(label = "College distance",
            value = collegeDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { collegeDistance.floatValue = it }),

        SliderData(label = "Post office distance",
            value = postOfficeDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { postOfficeDistance.floatValue = it }),

        SliderData(label = "Clinic distance",
            value = clinicDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { clinicDistance.floatValue = it }),

        SliderData(label = "School distance",
            value = schoolDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { schoolDistance.floatValue = it }),

        SliderData(label = "Pharmacy distance",
            value = pharmacyDistance,
            unit = Units.KILOMETERS,
            moreSteps = true,
            valueRange = 0f..5f,
            onValueChange = { pharmacyDistance.floatValue = it }),
    )
}