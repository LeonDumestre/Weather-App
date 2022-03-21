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

class AllDataAsyncTask : AsyncTask<Any, Void, String>() {

    private val MAX_DATA = 39
    private val HOST : String =
        "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&rows=$MAX_DATA&sort=-forecast&q=not(%23null(total_water_precipitation))"
    private var jsonList : MutableList<String> = mutableListOf()
    private var tmpList : MutableList<WeatherAllData> = mutableListOf()
    private val communeList = CommuneSingleton.list
    private lateinit var splashScreenActivity: WeakReference<SplashScreenActivity>

    override fun doInBackground(vararg params: Any?): String {
        splashScreenActivity = WeakReference<SplashScreenActivity>(params[0] as SplashScreenActivity?)

        Log.e("APPLOG", "Début des requêtes")

        //Requêtes des principales villes de France
        for (item in communeList)  {
            val finalHOST = HOST + "&geofilter.distance=" + item.latitude + "," + item.longitude + ",964"
            Log.e("APPLOG", finalHOST)
            val urlConnection = URL(finalHOST).openConnection() as HttpURLConnection
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
            if (nhits.toInt() > 0) {
                val records = JSONObject(jsonItem).getJSONArray("records")
                val firstDay = getDay(records.getJSONObject(1).getJSONObject("fields").optString("forecast"))
                var nbData = 0

                while (nbData < MAX_DATA) {
                    val period = getPeriod(records.getJSONObject(nbData).getJSONObject("fields").optString("forecast"))

                    //Log.d("APPLOG", "PERIOD = $period")
                    while (nbData < MAX_DATA &&
                        period == getPeriod((records.getJSONObject(nbData).getJSONObject("fields").optString("forecast")))
                    ) {
                        //Log.d("APPLOG", "current date : " + records.getJSONObject(nbData).getJSONObject("fields").optString("forecast"))
                        val item = records.getJSONObject(nbData).getJSONObject("fields")
                        tmpList.add(
                            WeatherAllData(
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
                        )
                        //Log.e("APPLOG", ""+nbData)
                        nbData++
                    }

                    if (tmpList.size > 0) {
                        val realDay = getDay(tmpList[tmpList.size-1].forecast)
                        val indDay = realDay - firstDay
                        if (WeatherSingleton.weatherList.isEmpty() || WeatherSingleton.weatherList.size <= indDay) {
                            WeatherSingleton.weatherList.add(DayWeather())
                            WeatherSingleton.weatherList[indDay].day = realDay
                        }
                        if (WeatherSingleton.weatherList[indDay].dayList.isEmpty() || WeatherSingleton.weatherList[indDay].dayList.size <= period) {
                            WeatherSingleton.weatherList[indDay].dayList.add(PeriodWeather())
                            WeatherSingleton.weatherList[indDay].dayList[period].period = period
                        }
                        Log.e("APPLOG", "day = $indDay")
                        Log.e("APPLOG", "period = $period")
                        WeatherSingleton.weatherList[indDay].dayList[period].periodList.add(WeatherData(tmpList, period))
                        Log.i("APPLOG", "commune : " +  WeatherSingleton.weatherList[indDay].dayList[period].periodList[WeatherSingleton.weatherList[indDay].dayList[period].periodList.size-1].communeLocation.commune)
                        tmpList.clear()
                    }
                }

            }
        }
        startMainActivity()
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