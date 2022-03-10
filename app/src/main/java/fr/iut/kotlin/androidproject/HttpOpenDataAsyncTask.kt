package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL

class HttpOpenDataAsyncTask : AsyncTask<Any, Void, String>() {

    private val host : String = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&q=&rows=100&sort=forecast"
    private lateinit var location : Location
    private lateinit var listView : ListView
    private lateinit var alertDialog: AlertDialog
    private lateinit var context: Context

    override fun doInBackground(vararg params: Any?): String {
        location = params[0] as Location
        listView = params[1] as ListView
        alertDialog = params[2] as AlertDialog
        context = params[3] as Context

        val finalHost = host + "&geofilter.distance=" + location.latitude + "," + location.longitude + ",1500"
        val url = URL(finalHost)
        val urlConnection = url.openConnection() as HttpURLConnection
        Log.e("APPLOG", finalHost)

        var text = ""
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            text = input.readLine()
            input.close()
        }
        urlConnection.disconnect()
        return text
    }

    //total_water_precipitation : 10ème de pouces/heure
    //x / 40 * 24 et ça donne des cm
    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: String?) {
        alertDialog.dismiss()
        val nhits = JSONObject(result).getString("nhits")
        Log.e("APPLOG", "nb Data : $nhits")
        val records = JSONObject(result).getJSONArray("records")

        val weatherList : MutableList<WeatherData> = mutableListOf()
        for (i in 0 until 40) {
            val item = records.getJSONObject(i).getJSONObject("fields")
            val wdData = WeatherData(item.getString("forecast"), BigDecimal(item.getString("2_metre_temperature")).setScale(2, RoundingMode.HALF_EVEN).toString())
            weatherList.add(wdData)
        }

        val adapter = WeatherListAdapter(context, weatherList)
        listView.adapter = adapter
    }

}
