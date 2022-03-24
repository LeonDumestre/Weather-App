package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.WeatherData
import java.time.LocalDateTime

object WeatherSingleton {
    var weatherList : MutableList<DayWeather> = arrayListOf()
}

data class DayWeather (
    var day : Int,
    var month : String,
    var dayList : MutableList<PeriodWeather> = arrayListOf()
)

data class PeriodWeather (
    var period : String,
    var periodList : MutableList<WeatherData> = arrayListOf()
)