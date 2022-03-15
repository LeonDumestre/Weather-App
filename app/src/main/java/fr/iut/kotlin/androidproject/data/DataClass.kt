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

data class WeatherData(
    var date: String,
    var icon : Int,
    var temp : Double,
    var minTemp : Int,
    var maxTemp : Int,
    var precipitation: Double,
    var windSpeed: Double
    ) : Serializable

data class CommuneLocation(
    var name : String,
    var latitude : Double,
    var longitude : Double
)