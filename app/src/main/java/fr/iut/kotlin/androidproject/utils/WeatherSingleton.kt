package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData

object WeatherSingleton {
    var weatherList : MutableList<WeatherData> = ArrayList()
}