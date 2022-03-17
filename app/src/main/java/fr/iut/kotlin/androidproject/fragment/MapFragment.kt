package fr.iut.kotlin.androidproject.fragment

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.utils.WeatherSingleton
import kotlin.math.ln

class MapFragment() : Fragment(), GoogleMap.OnMarkerClickListener {

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.setAllGesturesEnabled(false)

        val center = LatLng(46.227638, 2.213749)
        val zoomLevel = (ln(40000 / (600000.0f * 3.5 / 1000 / 2)) / ln(2.0)).toFloat()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))

        var currentCommune = ""
        for (item in WeatherSingleton.weatherList) {
            if (currentCommune != item.communeLocation.commune) {
                val icon: Bitmap = BitmapFactory.decodeResource(resources, item.icon)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(item.communeLocation.latitude, item.communeLocation.longitude))
                        .title(item.communeLocation.commune)
                        .icon(fromBitmap(createScaledBitmap(icon, 80, 80, false)))
                )
                currentCommune = item.communeLocation.commune
            }
        }
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerClick(m: Marker): Boolean {
        Toast.makeText(activity, "Marker Click", Toast.LENGTH_SHORT).show()
        return false
    }
}