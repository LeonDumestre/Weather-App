package fr.iut.kotlin.androidproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.WeatherListAdapter
import fr.iut.kotlin.androidproject.data.WeatherData
import fr.iut.kotlin.androidproject.utils.MyLocationSingleton
import fr.iut.kotlin.androidproject.utils.WeatherSingleton

class WeatherFragment : Fragment() {

    private lateinit var fragmentView : View
    private lateinit var lvWeatherInfo: ListView
    private lateinit var tvCommune: TextView
    private lateinit var adapter: WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WeatherListAdapter(activity as Context, MyLocationSingleton.weatherList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_weather, container, false)

        tvCommune = fragmentView.findViewById(R.id.tv_commune)
        tvCommune.text = MyLocationSingleton.commune
        lvWeatherInfo = fragmentView.findViewById(R.id.weather_list)
        lvWeatherInfo.adapter = adapter

        return fragmentView
    }
}