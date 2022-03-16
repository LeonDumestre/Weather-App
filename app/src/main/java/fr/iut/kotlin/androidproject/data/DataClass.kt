package fr.iut.kotlin.androidproject.data

import java.io.Serializable

data class WeatherAllData(
    var date: String,
    var temperature: String,
    var humidity: String,
    var precipitation: String,
    var windSpeed: String,
    var solarRadiation: String
)


data class CommuneLocation(
    var name : String,
    var latitude : Double,
    var longitude : Double
)