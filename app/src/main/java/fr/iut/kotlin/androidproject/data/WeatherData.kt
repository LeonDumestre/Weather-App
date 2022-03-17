package fr.iut.kotlin.androidproject.data

import fr.iut.kotlin.androidproject.R
import java.math.BigDecimal
import java.math.RoundingMode

class WeatherData() {
    var date : String = ""
    lateinit var communeLocation : CommuneLocation
    var icon : Int = 0
    var temp : Double = 0.0
    var minTemp : Int = 0
    var maxTemp : Int = 0
    var precipitation: Double = 0.0
    var windSpeed: Double = 0.0


    constructor(list: MutableList<WeatherAllData>, period: String) : this() {
        var temp = 0.0
        var minTemp = list[0].`2_metre_temperature`
        var maxTemp = list[0].`2_metre_temperature`
        var windSpeed = 0.0

        for (i in 0 until list.size) {
            temp += list[i].`2_metre_temperature`.toDouble()
            if (list[i].`2_metre_temperature`.toDouble() < minTemp)
                minTemp = list[i].`2_metre_temperature`.toDouble()
            else if (list[i].`2_metre_temperature`.toDouble() > maxTemp)
                maxTemp = list[i].`2_metre_temperature`.toDouble()

            windSpeed += list[i].wind_speed.toDouble()
        }

        val solarRadiation = (list[list.size-1].surface_net_solar_radiation - list[0].surface_net_solar_radiation) / list.size
        val precipitation = ((list[list.size-1].total_water_precipitation - list[0].total_water_precipitation) / list.size) / 4 * 24

        val icon: Int = when {
            solarRadiation > 2000000 -> R.drawable.ic_sun
            solarRadiation > 800000 && precipitation > 0.2 -> R.drawable.ic_sun_rain
            solarRadiation > 800000 -> R.drawable.ic_sun_cloud
            solarRadiation > 100000 && precipitation > 1 -> R.drawable.ic_double_rain
            solarRadiation > 100000 && precipitation > 0.2 -> R.drawable.ic_simple_rain
            solarRadiation > 100000 -> R.drawable.ic_simple_cloud
            else -> R.drawable.ic_moon_cloud
        }

        //TODO Date UTC ?
        this.date = list[list.size-1].forecast.substringBefore('T') + " " + period
        this.communeLocation = list[list.size - 1].commune_location
        this.icon = icon
        this.temp = BigDecimal(temp / list.size).setScale(1, RoundingMode.HALF_EVEN).toDouble()
        this.minTemp = BigDecimal(minTemp).setScale(0, RoundingMode.HALF_EVEN).toInt() -1
        this.maxTemp = BigDecimal(maxTemp).setScale(0, RoundingMode.HALF_EVEN).toInt() +1
        this.precipitation = list[list.size - 1].total_water_precipitation - list[0].total_water_precipitation
        this.windSpeed = windSpeed / list.size

    }

}