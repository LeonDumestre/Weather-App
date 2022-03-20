package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData

object WeatherSingleton {
    var weatherList : MutableList<DayWeather> = arrayListOf()
}

object DayWeather {
    var day : Int = -1
    var dayList : MutableList<PeriodWeather> = arrayListOf()
}

object PeriodWeather {
    var period : Int = -1
    var periodList : MutableList<WeatherData> = arrayListOf()
}