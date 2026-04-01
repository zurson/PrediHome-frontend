package com.betoniarze.predihome.presentation.ui.prediction.map

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.ui.splash.SplashScreen
import com.betoniarze.predihome.presentation.viewmodel.PredictionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.Collator
import java.util.Locale

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewMapView() {
    val predictionViewModel = PredictionViewModel()
    val navController = rememberNavController()
    MapView(navController, predictionViewModel)
}

@Composable
fun MapView (navController: NavHostController, predictionViewModel: PredictionViewModel) {
    val context = LocalContext.current

    val target = predictionViewModel.mapsData.latlng
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(target, 10f)
    }
    var centerCoordinates by remember { mutableStateOf(target) }
    var cityName by remember { mutableStateOf(predictionViewModel.mapsData.name) }
    var selectedCity by remember { mutableStateOf<City?>(null) }

    var distance by remember { mutableStateOf(predictionViewModel.mapsData.centreDistance) }
    var isButtonEnabled by remember { mutableStateOf(true) }

    var showOverlay by remember { mutableStateOf(true) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            centerCoordinates = cameraPositionState.position.target

            cityName = getCityNameFromCoordinates(context, centerCoordinates.latitude, centerCoordinates.longitude)

            val cityFiltered = majorPolishCities.find { it.name.equals(cityName, ignoreCase = true) }
            if (cityFiltered != null) {
                cityName = cityFiltered.name
                distance = calculateDistance(centerCoordinates.latitude, centerCoordinates.longitude, cityFiltered.latlng.latitude, cityFiltered.latlng.longitude)
                isButtonEnabled = true
            } else {
                cityName = "Unsupported region"
                distance = 0.0
                isButtonEnabled = false
            }
            predictionViewModel.updateMapsData(City(cityName, centerCoordinates, distance))
            selectedCity = cityFiltered
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if(showOverlay) SplashScreen { showOverlay = false }
        Box(modifier = Modifier.fillMaxSize().alpha(if (showOverlay) 0.0f else 1.0f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                )
            )

            CityInfoBox(
                selectedCity = selectedCity,
                distance = distance,
                latitude = centerCoordinates.latitude,
                longitude = centerCoordinates.longitude,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.map_box_padding_external_horizontal),
                        end = dimensionResource(R.dimen.map_box_padding_external_horizontal),
                        top = dimensionResource(R.dimen.map_box_padding_external_top)),
                cameraPositionState = cameraPositionState
            )

            MapCrosshair()

            Button(
                onClick = { navController.navigate(NavigationItem.PredictionParameters.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = dimensionResource(R.dimen.map_box_padding_external_horizontal),
                        end = dimensionResource(R.dimen.map_box_padding_external_horizontal),
                        bottom = dimensionResource(R.dimen.map_box_padding_external_top)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                enabled = isButtonEnabled
            ) {
                Text(text = "Next",
                    fontSize = dimensionResource(R.dimen.map_button).value.sp)
            }
        }
    }
}

@Composable
private fun MapCrosshair() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        VerticalDivider(
            modifier = Modifier
                .height(50.dp)
                .width(2.dp),
            color = Color.Black,
            thickness = 3.dp
        )
        HorizontalDivider(
            modifier = Modifier
                .width(50.dp)
                .height(2.dp),
            color = Color.Black,
            thickness = 3.dp
        )
    }
}

@Composable
fun CityDropdownMenu(
    selectedCity: City?,
    majorPolishCities: List<City>,
    onCitySelected: (LatLng) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val collator = Collator.getInstance(Locale("pl", "PL"))
    val sortedMajorPolishCities = majorPolishCities.sortedWith { city1, city2 ->
        collator.compare(city1.name, city2.name)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = selectedCity?.name ?: "Unsupported region",
                fontSize = dimensionResource(R.dimen.map_city_main).value.sp
            )
            Image(
                painter =
                if (expanded)
                    painterResource(id = R.drawable.round_arrow_drop_up_24) else
                        painterResource(id = R.drawable.round_arrow_drop_down_24),
                contentDescription = "Dropdown icon",
                modifier = Modifier.size(dimensionResource(R.dimen.map_city_main_icon))
                )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White) // Ustawienie koloru tła
                    .heightIn(max = dimensionResource(R.dimen.map_city_list_height))
            ) {
                sortedMajorPolishCities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(
                            text = city.name,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = dimensionResource(R.dimen.map_city_list_value).value.sp
                        ) },
                        onClick = {
                            expanded = false
                            onCitySelected(city.latlng)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CityInfoBox(
    selectedCity: City?,
    distance: Double,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState
) {
    Box(
        modifier = modifier
            .background(Color(0xCBFFFFFF), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display city name
//            Text(
//                text = cityName,
//                color = MaterialTheme.colorScheme.primary,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold
//            )
            CityDropdownMenu(
                selectedCity = selectedCity,
                majorPolishCities = majorPolishCities,
                onCitySelected = { newLatLng -> cameraPositionState.move(CameraUpdateFactory.newLatLng(newLatLng))}
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LAT:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = dimensionResource(R.dimen.map_coords_title).value.sp,
                            fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = String.format("%.4f", latitude),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = dimensionResource(R.dimen.map_coords_value).value.sp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LON:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = dimensionResource(R.dimen.map_coords_title).value.sp,
                            fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = String.format("%.4f", longitude),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = dimensionResource(R.dimen.map_coords_value).value.sp)
                    )
                }
            }

        }
    }
}


fun getCityNameFromCoordinates(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale("pl"))
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        addresses?.firstOrNull()?.locality ?: "Unknown"
    } catch (e: Exception) {
        "Error"
    }
}

fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val startLocation = Location("start").apply {
        latitude = lat1
        longitude = lon1
    }
    val endLocation = Location("end").apply {
        latitude = lat2
        longitude = lon2
    }
    return startLocation.distanceTo(endLocation).toDouble()
}
