package fr.iut.kotlin.androidproject.data

data class CommuneLocation(
    var commune : String,
    var latitude : Double,
    var longitude : Double
)

data class WeatherAllData(
    val `2_metre_temperature`: Double,
    val code_commune: String,
    val forecast: String,
    val maximum_temperature_at_2_metres: Double,
    val minimum_temperature_at_2_metres: Double,
    val commune_location: CommuneLocation,
    val relative_humidity: Double,
    val surface_latent_heat_flux: Int,
    val surface_net_solar_radiation: Int,
    val surface_net_thermal_radiation: Int,
    val surface_sensible_heat_flux: Int,
    val timestamp: String,
    val total_water_precipitation: Double,
    val wind_speed: Double
)