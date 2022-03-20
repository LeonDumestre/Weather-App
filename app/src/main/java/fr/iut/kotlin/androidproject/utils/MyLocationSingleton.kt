package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData

object MyLocationSingleton {
    var commune: String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var weatherList : MutableList<WeatherData> = arrayListOf()

    fun addLocation(com : String, lat : Double, long : Double) {
        this.commune = com
        this.latitude = lat
        this.longitude = long
    }

}