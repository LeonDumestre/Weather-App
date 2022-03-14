package fr.iut.kotlin.androidproject.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.WeatherData
import fr.iut.kotlin.androidproject.WeatherListAdapter
import fr.iut.kotlin.androidproject.asyncTask.HttpConnectServerAsyncTask
import fr.iut.kotlin.androidproject.asyncTask.HttpOpenDataAsyncTask

// TODO: Rename parameter arguments, choose names that match
// the fr.iut.kotlin.androidproject.fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fr.iut.kotlin.androidproject.fragment.
 */
class FirstFragment() : Fragment() {

    private var URL_TIME_TEXT = "http://worldtimeapi.org/api/timezone/Europe/paris"
    private lateinit var fragmentView : View
    private lateinit var tvCommune: TextView
    private lateinit var lvWeatherInfo: ListView
    private lateinit var weatherList: MutableList<WeatherData>
    private lateinit var adapter: WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherList = mutableListOf()
        adapter = WeatherListAdapter(activity as Context, weatherList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.e("APPLOG-Fragment", "CreateView")
        fragmentView = inflater.inflate(R.layout.fragment_first, container, false)

        tvCommune = fragmentView.findViewById(R.id.tv_commune)
        lvWeatherInfo = fragmentView.findViewById(R.id.weather_list)

        lvWeatherInfo.adapter = adapter

        return fragmentView
    }
}