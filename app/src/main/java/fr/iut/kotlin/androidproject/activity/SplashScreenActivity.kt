package fr.iut.kotlin.androidproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.asyncTask.HttpOpenDataAsyncTask
import fr.iut.kotlin.androidproject.utils.MyLocationSingleton
import java.util.*


class SplashScreenActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar.isIndeterminate = true

        val tvLoading = findViewById<TextView>(R.id.loading_text)
        tvLoading.text = "Chargement de la position..."
        getLastLocation()

        tvLoading.text = "Chargement des données..."
        HttpOpenDataAsyncTask().execute(this)
    }

    private fun getLastLocation() {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                createMyLocationSingleton(location)
            } else {
                getCurrentLocation()
            }
        }
    }


    private fun getCurrentLocation() {
        //Vérifier si la localisation est activée
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 100F, this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        locationManager.removeUpdates(this)
        createMyLocationSingleton(location)
    }

    private fun createMyLocationSingleton(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        MyLocationSingleton.addLocation(addresses[0].locality, location.latitude, location.longitude)
    }

}