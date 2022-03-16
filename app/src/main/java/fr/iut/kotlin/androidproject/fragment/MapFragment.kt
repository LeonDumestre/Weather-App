package fr.iut.kotlin.androidproject.fragment

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.data.CommuneLocation
import kotlin.math.ln

class MapFragment() : Fragment(), GoogleMap.OnMarkerClickListener {

    private val communeList : List<CommuneLocation> = listOf(
        CommuneLocation("Brest", 48.390394, -4.486076),
        CommuneLocation("Cherbourg-Octeville", 49.6337308, -1.622137),
        CommuneLocation("Rennes", 48.117266, -1.6777926),
        CommuneLocation("Nantes", 47.218371, -1.553621),
        CommuneLocation("La Rochelle", 46.160329, -1.151139),
        CommuneLocation("Bordeaux", 44.837789, -0.57918),
        CommuneLocation("Biarritz", 43.4831519, -1.558626),
        CommuneLocation("Rouen", 49.443232, 1.099971),
        CommuneLocation("Alençon", 48.432856, 0.091266),
        CommuneLocation("Tours", 47.394144, 0.68484),
        CommuneLocation("Limoges", 45.833619, 1.261105),
        CommuneLocation("Tarbes", 43.232951, 0.078082),
        CommuneLocation("Toulouse", 43.604652, 1.444209),
        CommuneLocation("Lille", 50.62925, 3.057256),
        CommuneLocation("Amiens", 49.894067, 2.295753),
        CommuneLocation("Paris", 48.856614, 2.3522219),
        CommuneLocation("Bourges", 47.081012, 2.398782),
        CommuneLocation("Aurillac", 44.930953, 2.444997),
        CommuneLocation("Perpignan", 42.6886591, 2.8948332),
        CommuneLocation("Reims", 49.258329, 4.031696),
        CommuneLocation("Auxerre", 47.798202, 3.573781),
        CommuneLocation("Vichy", 46.131859, 3.425488),
        CommuneLocation("Montpellier", 43.610769, 3.876716),
        CommuneLocation("Chaumont", 48.113748, 5.1392559),
        CommuneLocation("Chalon-sur-Saône", 46.780764, 4.853947),
        CommuneLocation("Lyon", 45.764043, 4.835659),
        CommuneLocation("Montélimar", 44.556944, 4.749496),
        CommuneLocation("Marseille", 43.296482, 5.36978),
        CommuneLocation("Strasbourg", 48.5734053, 7.7521113),
        CommuneLocation("Metz", 49.1193089, 6.1757156),
        CommuneLocation("Belfort", 47.639674, 6.863849),
        CommuneLocation("Bourg-Saint-Maurice", 45.618598, 6.769548),
        CommuneLocation("Gap", 44.566667, 6.083333),
        CommuneLocation("Nice", 43.7101728, 7.2619532),
        CommuneLocation("Ajaccio", 41.919229, 8.738635)
    )

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.setAllGesturesEnabled(false)

        val center = LatLng(46.227638, 2.213749)
        val zoomLevel = (ln(40000 / (600000.0f * 3.5 / 1000 / 2)) / ln(2.0)).toFloat()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))

        for (i in communeList.indices) {
            val icon : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_double_rain);
            googleMap.addMarker(MarkerOptions()
                .position(LatLng(communeList[i].latitude, communeList[i].longitude))
                .title(communeList[i].name)
                .icon(fromBitmap(createScaledBitmap(icon, 80, 80, false)))
            )
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