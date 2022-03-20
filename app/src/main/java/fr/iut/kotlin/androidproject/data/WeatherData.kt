package fr.iut.kotlin.androidproject.data

import android.os.Build
import androidx.annotation.RequiresApi
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.utils.MyLocationSingleton
import fr.iut.kotlin.androidproject.utils.WeatherSingleton
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class WeatherData() {
    lateinit var date : LocalDateTime
    lateinit var communeLocation : CommuneLocation
    var icon : Int = 0
    var temp : Double = 0.0
    var minTemp : Int = 0
    var maxTemp : Int = 0
    var precipitation: Double = 0.0
    var windSpeed: Double = 0.0


    @RequiresApi(Build.VERSION_CODES.O)
    constructor(list: MutableList<WeatherAllData>, period: Int) : this() {

        var temp = 0.0
        var minTemp = list[0].`2_metre_temperature`
        var maxTemp = list[0].`2_metre_temperature`
        var windSpeed = 0.0

        for (i in 0 until list.size) {
            temp += list[i].`2_metre_temperature`
            if (list[i].`2_metre_temperature` < minTemp)
                minTemp = list[i].`2_metre_temperature`
            else if (list[i].`2_metre_temperature` > maxTemp)
                maxTemp = list[i].`2_metre_temperature`

            windSpeed += list[i].wind_speed
        }
//total_water_precipitation : 10ème de pouces/heure
        //x / 40 * 24 et ça donne des cm
        val solarRadiation = (list[list.size-1].surface_net_solar_radiation - list[0].surface_net_solar_radiation) / list.size
        val precipitation = ((list[list.size-1].total_water_precipitation - list[0].total_water_precipitation) / list.size) / 4 * 24

        val icon: Int = when {
            solarRadiation > 2000000 -> R.drawable.ic_sun
            solarRadiation > 800000 && precipitation > 0.2 -> R.drawable.ic_sun_rain
            solarRadiation > 800000 -> R.drawable.ic_sun_cloud
            solarRadiation > 100000 && precipitation > 1 -> R.drawable.ic_double_rain
            solarRadiation > 100000 && precipitation > 0.2 -> R.drawable.ic_simple_rain
            period == 0 -> R.drawable.ic_moon
            else -> R.drawable.ic_simple_cloud
        }

        val cutDate = list[list.size-1].forecast.substringBefore('+')
        this.date = LocalDateTime.parse(cutDate).atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        this.communeLocation = list[list.size - 1].commune_location
        this.icon = icon
        this.temp = BigDecimal(temp / list.size).setScale(1, RoundingMode.HALF_EVEN).toDouble()
        this.minTemp = BigDecimal(minTemp).setScale(0, RoundingMode.HALF_EVEN).toInt() -1
        this.maxTemp = BigDecimal(maxTemp).setScale(0, RoundingMode.HALF_EVEN).toInt() +1
        this.precipitation = list[list.size - 1].total_water_precipitation - list[0].total_water_precipitation
        this.windSpeed = windSpeed / list.size
    }

}