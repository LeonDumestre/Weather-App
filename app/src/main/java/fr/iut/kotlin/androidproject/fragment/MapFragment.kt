package fr.iut.kotlin.androidproject.fragment

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.utils.WeatherSingleton
import kotlin.math.ln


class MapFragment : Fragment(), GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private lateinit var fragmentView : View
    private lateinit var ggMap : GoogleMap

    private lateinit var btn_day1 : Button
    private lateinit var btn_day2 : Button
    private lateinit var btn_period0 : Button
    private lateinit var btn_period1 : Button
    private lateinit var btn_period2 : Button
    private lateinit var btn_period3 : Button

    private var day = 0
    private var period = 0

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.setAllGesturesEnabled(false)
        ggMap = googleMap

        val center = LatLng(46.227638, 2.213749)
        val zoomLevel = (ln(40000 / (600000.0f * 3.5 / 1000 / 2)) / ln(2.0)).toFloat()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))

        val static = LatLngBounds(center, center)
        googleMap.setLatLngBoundsForCameraTarget(static)

        val layer = GeoJsonLayer(googleMap, R.raw.border, this.context)

        val style = layer.defaultPolygonStyle
        style.fillColor = Color.LTGRAY
        style.strokeColor = Color.BLACK
        style.strokeWidth = 2f

        layer.addLayerToMap()
        setMarkers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_map, container, false)
        btn_day1 = fragmentView.findViewById(R.id.map_day1)
        btn_day2 = fragmentView.findViewById(R.id.map_day2)
        btn_period0 = fragmentView.findViewById(R.id.map_period0)
        btn_period1 = fragmentView.findViewById(R.id.map_period1)
        btn_period2 = fragmentView.findViewById(R.id.map_period2)
        btn_period3 = fragmentView.findViewById(R.id.map_period3)

        btn_day1.isEnabled = false
        btn_day1.isClickable = false
        btn_period0.isEnabled = false
        btn_period0.isClickable = false

        btn_day1.setOnClickListener {
            lockButtonDay(btn_day1)
            day = 0
            setMarkers()
        }
        btn_day2.setOnClickListener {
            lockButtonDay(btn_day2)
            day = 1
            setMarkers()
        }
        btn_period0.setOnClickListener {
            lockButtonPeriod(btn_period0)
            period = 0
            setMarkers()
        }
        btn_period1.setOnClickListener {
            lockButtonPeriod(btn_period1)
            period = 1
            setMarkers()
        }
        btn_period2.setOnClickListener {
            lockButtonPeriod(btn_period2)
            period = 2
            setMarkers()
        }
        btn_period3.setOnClickListener {
            lockButtonPeriod(btn_period3)
            period = 3
            setMarkers()
        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMarkers() {
        ggMap.clear()
        var currentCommune = ""
        for (item in WeatherSingleton.weatherList[day].dayList[period].periodList) {
            if (currentCommune != item.communeLocation.commune) {
                val icon: Bitmap = BitmapFactory.decodeResource(resources, item.icon)
                ggMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(item.communeLocation.latitude, item.communeLocation.longitude))
                        .title(item.communeLocation.commune)
                        .icon(fromBitmap(createScaledBitmap(icon, 80, 80, false)))
                )
                currentCommune = item.communeLocation.commune
            }
        }
        ggMap.setOnMarkerClickListener(this)
    }

    private fun lockButtonDay(btn : Button) {
        btn_day1.isEnabled = true
        btn_day1.isClickable = true
        btn_day2.isEnabled = true
        btn_day2.isClickable = true

        btn.isEnabled = false
        btn.isClickable = false
    }

    private fun lockButtonPeriod(btn : Button) {
        btn_period0.isEnabled = true
        btn_period0.isClickable = true
        btn_period1.isEnabled = true
        btn_period1.isClickable = true
        btn_period2.isEnabled = true
        btn_period2.isClickable = true
        btn_period3.isEnabled = true
        btn_period3.isClickable = true

        btn.isEnabled = false
        btn.isClickable = false
    }

    override fun onMarkerClick(m: Marker): Boolean {

        return true
    }

    override fun onClick(v: View?) {

    }
}