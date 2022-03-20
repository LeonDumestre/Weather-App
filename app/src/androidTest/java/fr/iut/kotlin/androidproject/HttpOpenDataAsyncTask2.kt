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
import fr.iut.kotlin.androidproject.utils.*
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
    private var tmpList : MutableList<WeatherAllData> = mutableListOf()
    private val communeList = CommuneSingleton.list
    private lateinit var splashScreenActivity: WeakReference<SplashScreenActivity>

    override fun doInBackground(vararg params: Any?): String {
        splashScreenActivity = WeakReference<SplashScreenActivity>(params[0] as SplashScreenActivity?)

        Log.e("APPLOG", "Début des requêtes")

        //Requête de ma localisation
        var finalHost = host + "%23exact(commune,\"" + MyLocationSingleton.commune + "\")"
        var urlConnection = URL(finalHost).openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            Log.e("APPLOG", "Requête MyLocation")
            if (JSONObject(input.readLine()).optString("nhits") == "0") {
                //TODO Rechercher en fonction de la localisation si la ville n'existe pas
                //val finalHost = host + "&geofilter.distance=" + location.latitude + "," + location.longitude + ",1500"
            } else {
                jsonList.add(input.readLine())
            }
            input.close()
        }
        urlConnection.disconnect()

        //TODO il en manque
        //Requêtes des principales villes de France
        var nb = 0
        for (i in communeList.indices)  {
            if (nb == 0) {
                finalHost = "$host("
            }
            if (nb <= 4) {
                finalHost += "%23exact(commune," + communeList[i].commune + ")"
                if (nb < 4)
                    finalHost += "+OR+"
                nb++
            }
            else {
                finalHost += ")"
                Log.e("APPLOG", "Requête $i : $finalHost")
                urlConnection = URL(finalHost).openConnection() as HttpURLConnection
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    jsonList.add(input.readLine())
                    input.close()
                }
                urlConnection.disconnect()
                nb = 0
            }
        }
        //Envoie de la dernière requêtes des principales villes
        if (nb > 0) {
            finalHost += ")"
            urlConnection = URL(finalHost).openConnection() as HttpURLConnection
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
                jsonList.add(input.readLine())
                input.close()
            }
            urlConnection.disconnect()
        }

        Log.e("APPLOG", "Fin des requêtes")
        Log.e("APPLOG", "Size jsonList : " + jsonList.size)
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onPostExecute(result: String?) {


        for (jsonItem in jsonList) {
            val nhits = JSONObject(jsonItem).optString("nhits")

            var indData = 0
            if (nhits.toInt() > 0) {
                val records = JSONObject(jsonItem).getJSONArray("records")
                val firstDay = getDay(records.getJSONObject(1).getJSONObject("fields").optString("forecast"))

                while (indData < nhits.toInt()) {
                    val period = getPeriod((records.getJSONObject(indData).getJSONObject("fields").optString("forecast")))
                    while (period == getPeriod(records.getJSONObject(indData).getJSONObject("fields").optString("forecast"))) {
                        val item = records.getJSONObject(indData).getJSONObject("fields")
                        val wData = WeatherAllData(
                            CommuneSingleton.getCommuneLocation(item.optString("commune")),
                            item.optString("forecast"),
                            item.optDouble("2_metre_temperature"),
                            item.optDouble("maximum_temperature_at_2_metres"),
                            item.optDouble("minimum_temperature_at_2_metres"),
                            item.optDouble("relative_humidity"),
                            item.optInt("surface_net_solar_radiation"),
                            item.optDouble("total_water_precipitation"),
                            item.optDouble("wind_speed")
                        )
                        indData = getWeatherDataByForecast(jsonItem, wData, indData)
                    }

                    //TODO inititialiser les listes d'objets ?
                    if (tmpList.size > 0) {
                        val realDay = getDay(tmpList[0].forecast)
                        val indDay = realDay - firstDay
                        if (WeatherSingleton.weatherList.isEmpty() || WeatherSingleton.weatherList.size <= indDay) {
                            WeatherSingleton.weatherList.add(DayWeather)
                            WeatherSingleton.weatherList[indDay].day = realDay
                        }
                        if (WeatherSingleton.weatherList[indDay].dayList.isEmpty() || WeatherSingleton.weatherList[indDay].dayList.size <= period) {
                            WeatherSingleton.weatherList[indDay].dayList.add(PeriodWeather)
                            WeatherSingleton.weatherList[indDay].dayList[period].period = period
                        }
                        Log.e("APPLOG", "day = " + indDay)
                        Log.e("APPLOG", "period = " + period)
                        WeatherSingleton.weatherList[indDay].dayList[period].periodList.add(WeatherData(tmpList, period))
                        tmpList.clear()
                    }
                }
            }
        }
        startMainActivity()
    }

    private fun getWeatherDataByForecast(jsonItem : String, wData : WeatherAllData, indFirstData : Int) : Int {
        var indData = indFirstData
        var nbData = 1
        val records = JSONObject(jsonItem).getJSONArray("records")
        nbData++
        indData++

        while (wData.forecast == records.getJSONObject(indData).getJSONObject("fields").optString("forecast")) {
            val item = records.getJSONObject(indData).getJSONObject("fields")
            wData.`2_metre_temperature` += item.optDouble("2_metre_temperature")
            wData.maximum_temperature_at_2_metres += item.optDouble("maximum_temperature_at_2_metres")
            wData.minimum_temperature_at_2_metres += item.optDouble("minimum_temperature_at_2_metres")
            wData.relative_humidity += item.optDouble("relative_humidity")
            wData.surface_net_solar_radiation += item.optInt("surface_net_solar_radiation")
            wData.total_water_precipitation += item.optDouble("total_water_precipitation")
            wData.wind_speed += item.optDouble("wind_speed")
            nbData++
            indData++
        }
        wData.`2_metre_temperature` /= nbData
        wData.maximum_temperature_at_2_metres /= nbData
        wData.minimum_temperature_at_2_metres /= nbData
        wData.relative_humidity /= nbData
        wData.surface_net_solar_radiation /= nbData
        wData.total_water_precipitation /= nbData
        wData.wind_speed /= nbData
        tmpList.add(wData)

        return indData
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriod(forecast : String): Int {
        val cutDate = forecast.substringBefore('+')
        val date = LocalDateTime.parse(cutDate).atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        return when (date.hour) {
            in 8..12 -> 1       //Morning
            in 13..17 -> 2      //Afternoon
            in 18..22 -> 3      //Evening
            else -> 0           //Night
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDay(forecast : String): Int {
        val cutDate = forecast.substringBefore('+')
        val date = LocalDateTime.parse(cutDate).atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        return date.dayOfMonth
    }

    private fun startMainActivity() {
        val i = Intent(splashScreenActivity.get(), MainActivity::class.java)
        splashScreenActivity.get()?.startActivity(i)
        splashScreenActivity.get()?.finish()
    }

}