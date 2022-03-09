package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.location.Location
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpOpenDataAsyncTask : AsyncTask<Any, Void, String>() {

    private val host : String = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&q=&rows=100&sort=forecast"
    private lateinit var location : Location
    @SuppressLint("StaticFieldLeak")
    private lateinit var textView : TextView
    private lateinit var alertDialog: AlertDialog

    override fun doInBackground(vararg params: Any?): String {
        location = params[0] as Location
        textView = params[1] as TextView
        alertDialog = params[2] as AlertDialog

        val finalHost = host + "&geofilter.distance=" + location.latitude + "," + location.longitude + ",1500"

        val url = URL(finalHost)
        val urlConnection = url.openConnection() as HttpURLConnection

        var text = ""
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            text = input.readLine()
            Log.d("AsyncTask", "flux = $text")
            input.close()
        }
        urlConnection.disconnect()
        return text
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: String?) {
        alertDialog.dismiss()
        textView.text = "Nb de data : " + JSONObject(result).getString("nhits");

    }
}