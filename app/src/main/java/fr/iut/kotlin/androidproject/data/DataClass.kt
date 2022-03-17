package fr.iut.kotlin.androidproject.data

data class CommuneLocation(
    var commune : String,
    var latitude : Double,
    var longitude : Double
)

data class WeatherAllData(
    var commune_location: CommuneLocation,
    var code_commune: String,
    var forecast: String,
    var `2_metre_temperature`: Double,
    var maximum_temperature_at_2_metres: Double,
    var minimum_temperature_at_2_metres: Double,
    var relative_humidity: Double,
    var surface_latent_heat_flux: Int,
    var surface_net_solar_radiation: Int,
    var surface_net_thermal_radiation: Int,
    var surface_sensible_heat_flux: Int,
    var total_water_precipitation: Double,
    var wind_speed: Double,
    var timestamp: String
)