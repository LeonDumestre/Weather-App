package fr.iut.kotlin.androidproject.utils

object MyLocationSingleton {
    var commune: String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    fun add(com : String, lat : Double, long : Double) {
        this.commune = com
        this.latitude = lat
        this.longitude = long
    }

}