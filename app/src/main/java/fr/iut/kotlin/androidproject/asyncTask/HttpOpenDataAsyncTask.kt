package fr.iut.kotlin.androidproject.asyncTask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import fr.iut.kotlin.androidproject.activity.MainActivity
import fr.iut.kotlin.androidproject.activity.SplashScreenActivity
import fr.iut.kotlin.androidproject.data.WeatherAllData
import fr.iut.kotlin.androidproject.data.WeatherData
import fr.iut.kotlin.androidproject.utils.CommuneSingleton
import fr.iut.kotlin.androidproject.utils.WeatherSingleton
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class HttpOpenDataAsyncTask : AsyncTask<Any, Void, String>() {

    private val host : String = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&rows=9999&sort=-forecast&q=not(%23null(total_water_precipitation))+AND+"
    private var jsonList : MutableList<String> = mutableListOf()
    private val communeList = CommuneSingleton.list
    private lateinit var splashScreenActivity: WeakReference<SplashScreenActivity>

    override fun doInBackground(vararg params: Any?): String {
        splashScreenActivity = WeakReference<SplashScreenActivity>(params[0] as SplashScreenActivity?)

        Log.e("APPLOG", "Début des requêtes")
        //val finalHost = host + "&geofilter.distance=" + location.latitude + "," + location.longitude + ",1500"
        for (item in communeList) {
            val finalHost = host + "%23exact(commune,\"" + item.commune + "\")"
            val urlConnection = URL(finalHost).openConnection() as HttpURLConnection
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
                jsonList.add(input.readLine())
                input.close()
            }
            urlConnection.disconnect()
        }
        Log.e("APPLOG", "Fin des requêtes")
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onPostExecute(result: String?) {

        val weatherDataList : MutableList<WeatherAllData> = mutableListOf()
        Log.e("APPLOG", "Size jsonList : " + jsonList.size)
        for (i in 0 until jsonList.size) {
            val nhits = JSONObject(jsonList[i]).getString("nhits")

            if (nhits.toInt() > 0) {
                val records = JSONObject(jsonList[i]).getJSONArray("records")

                var item = records.getJSONObject(0).getJSONObject("fields")
                var lastForecast = item.optString("forecast")
                var weatherData = WeatherAllData(
                    CommuneSingleton.getCommuneLocation(item.optString("commune")),
                    item.optString("code_commune"),
                    item.optString("forecast"),
                    item.optDouble("2_metre_temperature"),
                    item.optDouble("maximum_temperature_at_2_metres"),
                    item.optDouble("minimum_temperature_at_2_metres"),
                    item.optDouble("relative_humidity"),
                    item.optInt("surface_latent_heat_flux"),
                    item.optInt("surface_net_solar_radiation"),
                    item.optInt("surface_net_thermal_radiation"),
                    item.optInt("surface_sensible_heat_flux"),
                    item.optDouble("total_water_precipitation"),
                    item.optDouble("wind_speed"),
                    item.optString("timestamp")
                )

                var nbData = 1
                for (j in 1 until nhits.toInt()) {
                    item = records.getJSONObject(j).getJSONObject("fields")

                    if (j > 1 && item.optString("forecast") != lastForecast) {
                        weatherData.`2_metre_temperature` /= nbData
                        weatherData.maximum_temperature_at_2_metres /= nbData
                        weatherData.minimum_temperature_at_2_metres /= nbData
                        weatherData.relative_humidity /= nbData
                        weatherData.surface_latent_heat_flux /= nbData
                        weatherData.surface_net_solar_radiation /= nbData
                        weatherData.surface_net_thermal_radiation /= nbData
                        weatherData.surface_sensible_heat_flux /= nbData
                        weatherData.total_water_precipitation /= nbData
                        weatherData.wind_speed /= nbData
                        weatherDataList.add(weatherData)

                        weatherData = WeatherAllData(
                            CommuneSingleton.getCommuneLocation(item.optString("commune")),
                            item.optString("code_commune"),
                            item.optString("forecast"),
                            item.optDouble("2_metre_temperature"),
                            item.optDouble("maximum_temperature_at_2_metres"),
                            item.optDouble("minimum_temperature_at_2_metres"),
                            item.optDouble("relative_humidity"),
                            item.optInt("surface_latent_heat_flux"),
                            item.optInt("surface_net_solar_radiation"),
                            item.optInt("surface_net_thermal_radiation"),
                            item.optInt("surface_sensible_heat_flux"),
                            item.optDouble("total_water_precipitation"),
                            item.optDouble("wind_speed"),
                            item.optString("timestamp")
                        )
                        nbData = 1
                    } else {
                        weatherData.`2_metre_temperature` += item.optDouble("2_metre_temperature")
                        weatherData.maximum_temperature_at_2_metres += item.optDouble("maximum_temperature_at_2_metres")
                        weatherData.minimum_temperature_at_2_metres += item.optDouble("minimum_temperature_at_2_metres")
                        weatherData.relative_humidity += item.optDouble("relative_humidity")
                        weatherData.surface_latent_heat_flux += item.optInt("surface_latent_heat_flux")
                        weatherData.surface_net_solar_radiation += item.optInt("surface_net_solar_radiation")
                        weatherData.surface_net_thermal_radiation += item.optInt("surface_net_thermal_radiation")
                        weatherData.surface_sensible_heat_flux += item.optInt("surface_sensible_heat_flux")
                        weatherData.total_water_precipitation += item.optInt("total_water_precipitation")
                        weatherData.wind_speed += item.optDouble("wind_speed")
                        nbData++
                    }
                    lastForecast = weatherData.forecast
                }

                if (nbData > 1) {
                    weatherData.`2_metre_temperature` /= nbData
                    weatherData.maximum_temperature_at_2_metres /= nbData
                    weatherData.minimum_temperature_at_2_metres /= nbData
                    weatherData.relative_humidity /= nbData
                    weatherData.surface_latent_heat_flux /= nbData
                    weatherData.surface_net_solar_radiation /= nbData
                    weatherData.surface_net_thermal_radiation /= nbData
                    weatherData.surface_sensible_heat_flux /= nbData
                    weatherData.total_water_precipitation /= nbData
                    weatherData.wind_speed /= nbData
                    weatherDataList.add(weatherData)
                }
            }
        }

        weatherDataList.sortBy { it.commune_location.commune }
        Log.e("APPLOG", "List size : " + weatherDataList.size)

        val nightList : MutableList<WeatherAllData> = mutableListOf()       //0
        val morningList : MutableList<WeatherAllData> = mutableListOf()     //1
        val afternoonList : MutableList<WeatherAllData> = mutableListOf()   //2
        val eveningList : MutableList<WeatherAllData> = mutableListOf()     //3

        var firstVal = true
        for (i in 0 until weatherDataList.size) {
            when (getTimeZone(weatherDataList[i].forecast)) {
                0 -> {
                    if (!firstVal && getTimeZone(weatherDataList[i-1].forecast) != getTimeZone(weatherDataList[i].forecast))
                        addWeatherData(eveningList, "Soir")
                    nightList.add(weatherDataList[i])
                }
                1 -> {
                    if (!firstVal && getTimeZone(weatherDataList[i-1].forecast) != getTimeZone(weatherDataList[i].forecast))
                        addWeatherData(nightList, "Nuit")
                    morningList.add(weatherDataList[i])
                }
                2 -> {
                    if (!firstVal && getTimeZone(weatherDataList[i-1].forecast) != getTimeZone(weatherDataList[i].forecast))
                        addWeatherData(morningList, "Matin")
                    afternoonList.add(weatherDataList[i])
                }
                3 -> {
                    if (!firstVal && getTimeZone(weatherDataList[i-1].forecast) != getTimeZone(weatherDataList[i].forecast))
                        addWeatherData(afternoonList, "Après-midi")
                    eveningList.add(weatherDataList[i])
                }
            }
            firstVal = false
        }
        startMainActivity()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeZone(sDateUTC : String): Int {
        val cutDate = sDateUTC.substringBefore('+')
        val date = LocalDateTime.parse(cutDate).atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

        return when (date.hour) {
            in 8..12 -> 1       //Morning
            in 13..17 -> 2      //Afternoon
            in 18..22 -> 3      //Evening
            else -> 0           //Night
        }
    }

    //total_water_precipitation : 10ème de pouces/heure
    //x / 40 * 24 et ça donne des cm
    private fun addWeatherData(list : MutableList<WeatherAllData>, period: String) {
        if (list.size > 0) {
            WeatherSingleton.weatherList.add(WeatherData(list, period))
            list.clear()
        }
    }

    private fun startMainActivity() {
        val i = Intent(splashScreenActivity.get(), MainActivity::class.java)
        splashScreenActivity.get()?.startActivity(i)
        splashScreenActivity.get()?.finish()
    }

}