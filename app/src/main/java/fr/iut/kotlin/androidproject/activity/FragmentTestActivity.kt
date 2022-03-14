package fr.iut.kotlin.androidproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.fragment.FirstFragment
import fr.iut.kotlin.androidproject.fragment.SecondFragment
import fr.iut.kotlin.androidproject.fragment.ThirdFragment

class FragmentTestActivity : AppCompatActivity(), LocationListener {

    private lateinit var location: Location
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_test)

        getLocation()
    }

    private fun getLocation() {
        //Vérifier si la localisation est activée
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            setProgressDialog()
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 100F, this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(loc: Location) {
        Toast.makeText(
            this,
            "Latitude: " + location.latitude + " , Longitude: " + location.longitude,
            Toast.LENGTH_LONG
        ).show()
        locationManager.removeUpdates(this)
        setFragments()
    }

    private fun setFragments() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        val firstFragment= FirstFragment()
        val secondFragment= SecondFragment()
        val thirdFragment= ThirdFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.location ->setCurrentFragment(firstFragment)
                R.id.map ->setCurrentFragment(secondFragment)
                R.id.settings ->setCurrentFragment(thirdFragment)
            }
            true
        }
    }

    @SuppressLint("SetTextI18n")
    fun setProgressDialog() {
        val loading: AlertDialog.Builder = AlertDialog.Builder(this)
        loading.setCancelable(false)
        val inflater: LayoutInflater = this.layoutInflater
        loading.setView(inflater.inflate(R.layout.custom_dialog_loading, null))

        // Displaying the dialog
        alertDialog = loading.create()
        alertDialog.show()
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

}