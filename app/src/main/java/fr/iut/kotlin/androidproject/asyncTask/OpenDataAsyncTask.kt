package fr.iut.kotlin.androidproject.asyncTask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import fr.iut.kotlin.androidproject.activity.MainActivity
import fr.iut.kotlin.androidproject.activity.SplashScreenActivity
import fr.iut.kotlin.androidproject.data.CommuneLocation
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

class OpenDataAsyncTask : AsyncTask<Any, Void, String>() {

    private val HOST : String =
        "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&rows=500&sort=-forecast&q=not(%23null(total_water_precipitation))"
    private var jsonList : MutableList<String> = mutableListOf()
    private val communeList = CommuneSingleton.list
    private lateinit var splashScreenActivity: WeakReference<SplashScreenActivity>

    override fun doInBackground(vararg params: Any?): String {
        splashScreenActivity = WeakReference<SplashScreenActivity>(params[0] as SplashScreenActivity?)
        val myLocation : CommuneLocation = params[1] as CommuneLocation

        Log.e("APPLOG", "Début des requêtes")

        var finalHOST = HOST + "&geofilter.distance=" + myLocation.latitude + "," + myLocation.longitude + ",1500"
        Log.e("APPLOG", finalHOST)
        var urlConnection = URL(finalHOST).openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            jsonList.add(input.readLine())
            input.close()
        }
        urlConnection.disconnect()

        //Requêtes des principales villes de France
        for (item in communeList)  {
            finalHOST = HOST + "&geofilter.distance=" + item.latitude + "," + item.longitude + ",1500"
            Log.e("APPLOG", finalHOST)
            urlConnection = URL(finalHOST).openConnection() as HttpURLConnection
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
        initWeatherSingleton()

        for (jsonItem in jsonList) {
            val tmpList : MutableList<WeatherAllData> = mutableListOf()
            val nhits = JSONObject(jsonItem).optString("nhits")

            if (nhits.toInt() > 0) {
                val records = JSONObject(jsonItem).getJSONArray("records")
                var ind = 0

                while (ind < nhits.toInt()) {
                    val item = records.getJSONObject(ind).getJSONObject("fields")
                    if (tmpList.size == 0 || item.optString("forecast") != tmpList[tmpList.size - 1].forecast) {
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
                    }
                    ind++
                }
                splitDataIntoPeriods(tmpList)
            }
        }
        startMainActivity()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initWeatherSingleton() {
        val date = LocalDateTime.now()
        for (day in 0 until 2) {
            WeatherSingleton.weatherList.add(DayWeather(date.plusDays(day.toLong()).dayOfMonth, date.plusDays(day.toLong()).month.toString()))
            for (period in 0 until 4) {
                var strPeriod = ""
                when (period) {
                    0 -> strPeriod = "Nuit"
                    1 -> strPeriod = "Matin"
                    2 -> strPeriod = "Après-Midi"
                    3 -> strPeriod = "Soir"
                }
                WeatherSingleton.weatherList[day].dayList.add(PeriodWeather(strPeriod))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun splitDataIntoPeriods(list : MutableList<WeatherAllData>) {

        if (list.size > 0) {
            val tmpList : MutableList<WeatherAllData> = mutableListOf()
            var indDay = 0
            var period = 0

            for (item in list) {
                if (getPeriod(item.forecast) == period) {
                    tmpList.add(item)
                } else {
                    addWeatherData(tmpList, indDay, period)
                    tmpList.clear()
                    period = (period + 1) % 4
                    if (period == 0)
                        indDay++
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addWeatherData(list : MutableList<WeatherAllData>, indDay : Int, period : Int) {
        WeatherSingleton.weatherList[indDay].dayList[period].periodList.add(WeatherData(list, period))
        Log.i("APPLOG", "commune : " +  WeatherSingleton.weatherList[indDay].dayList[period].periodList[WeatherSingleton.weatherList[indDay].dayList[period].periodList.size-1].communeLocation.commune)
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

    private fun startMainActivity() {
        val i = Intent(splashScreenActivity.get(), MainActivity::class.java)
        splashScreenActivity.get()?.startActivity(i)
        splashScreenActivity.get()?.finish()
    }

}