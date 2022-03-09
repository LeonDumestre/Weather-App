package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpConnectServerAsyncTask : AsyncTask<Any, Void, String>() {

    private lateinit var host : String
    @SuppressLint("StaticFieldLeak")
    private lateinit var textView : TextView

    override fun doInBackground(vararg params: Any?): String {
        host = params[0] as String
        textView = params[1] as TextView

        val url = URL(host)
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

    override fun onPostExecute(result: String?) {
        textView.text = JSONObject(result).getString("datetime");

    }
}