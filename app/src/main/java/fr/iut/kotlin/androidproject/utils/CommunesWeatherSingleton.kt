package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData

object CommunesWeatherSingleton {
    var list : MutableList<WeatherData> = arrayListOf()
}