package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData

object WeatherSingleton {
    var weatherList : MutableList<DayWeather> = arrayListOf()
}

data class DayWeather (
    var day : Int = -1,
    var dayList : MutableList<PeriodWeather> = arrayListOf()
)

data class PeriodWeather (
    var period : Int = -1,
    var periodList : MutableList<WeatherData> = arrayListOf()
)