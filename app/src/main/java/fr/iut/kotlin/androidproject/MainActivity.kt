package fr.iut.kotlin.androidproject

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener, LocationListener {

    private var URL_OPENDATA = "https://public.opendatasoft.com/explore/dataset/arome-0025-enriched/information/?disjunctive.commune&disjunctive.code_commune"
    private var URL_TIME_TEXT = "http://worldtimeapi.org/api/timezone/Europe/paris"
    private var URL_METEO_TEXT = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&q=&facet=commune&facet=code_commune"
    private lateinit var tvDate : TextView
    private lateinit var btDate : Button
    private lateinit var btHtml : Button
    private lateinit var btGoogle : Button
    private lateinit var btMeteo : Button
    private lateinit var webView : WebView
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btDate = findViewById(R.id.btDate)
        tvDate = findViewById(R.id.tvDate)
        btHtml = findViewById(R.id.btTextHtml)
        btGoogle = findViewById(R.id.btGoogle)
        btMeteo = findViewById(R.id.btMeteo)
        webView = findViewById(R.id.webView)

        btDate.setOnClickListener(this)
        btHtml.setOnClickListener(this)
        btGoogle.setOnClickListener(this)
        btMeteo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btDate -> {
                HttpConnectServerAsyncTask().execute(URL_TIME_TEXT, tvDate)
            }

            R.id.btTextHtml -> {
                val strHtml = "<html><body><strong> <em>Ceci est un texte au format HTML </em> </strong></br>qui s'affiche très simplement</body></html>";
                webView.loadData(strHtml , "text/html; charset=utf-8", "UTF-8")
            }

            R.id.btGoogle -> {
                webView.loadUrl(URL_OPENDATA)
                webView.webViewClient = WebViewClient()
            }

            R.id.btMeteo -> {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        location.accuracy = 1F;
        tvDate.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        Toast.makeText(this, "Latitude: " + location.latitude + " , Longitude: " + location.longitude, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}