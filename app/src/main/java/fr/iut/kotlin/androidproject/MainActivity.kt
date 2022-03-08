package fr.iut.kotlin.androidproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener, LocationListener {

    private var URL_TIME_TEXT = "http://worldtimeapi.org/api/timezone/Europe/paris"
    private lateinit var tvDate : TextView
    private lateinit var btDate : Button
    private lateinit var btHtml : Button
    private lateinit var btGoogle : Button
    private lateinit var btMeteo : Button
    private lateinit var tvInfo : TextView
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btDate = findViewById(R.id.btDate)
        tvDate = findViewById(R.id.tvDate)
        btHtml = findViewById(R.id.btTextHtml)
        btGoogle = findViewById(R.id.btGoogle)
        btMeteo = findViewById(R.id.btMeteo)
        tvInfo = findViewById(R.id.tvInfo)

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
            }

            R.id.btGoogle -> {
            }

            R.id.btMeteo -> {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        //TODO Retester les droits
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, this)
            } catch(ex: SecurityException) {
                Log.d("APPLOG", "Security Exception, no location available")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        tvInfo.text = "Latitude: " + location.latitude + ", Longitude: " + location.longitude
        Toast.makeText(this, "Latitude: " + location.latitude + "\nLongitude: " + location.longitude, Toast.LENGTH_LONG).show()
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