package com.betoniarze.predihome.presentation.ui.prediction.map

import com.google.android.gms.maps.model.LatLng

data class City(
    var name: String = "",
    var latlng: LatLng = LatLng(51.7592, 19.4560),
    private var _centreDistance: Double = 0.0
) {
    var centreDistance: Double
        get() = _centreDistance / 1000
        set(value) {
            _centreDistance = value
        }
}

val majorPolishCities = listOf(
    City("Wrocław", LatLng(51.1079, 17.0385)),
    City("Poznań", LatLng(52.4064, 16.9252)),
    City("Gdańsk", LatLng(54.3520, 18.6466)),
    City("Katowice", LatLng(50.2649, 19.0238)),
    City("Łódź", LatLng(51.7592, 19.4560)),
    City("Bydgoszcz", LatLng(53.1235, 18.0084)),
    City("Częstochowa", LatLng(50.8110, 19.1203)),
    City("Szczecin", LatLng(53.4285, 14.5528)),
    City("Gdynia", LatLng(54.5189, 18.5305)),
    City("Warszawa", LatLng(52.2297, 21.0122)),
    City("Lublin", LatLng(51.2465, 22.5684)),
    City("Kraków", LatLng(50.0647, 19.9450)),
    City("Rzeszów", LatLng(50.0412, 21.9991)),
    City("Radom", LatLng(51.4027, 21.1471)),
    City("Białystok", LatLng(53.1325, 23.1688))
)