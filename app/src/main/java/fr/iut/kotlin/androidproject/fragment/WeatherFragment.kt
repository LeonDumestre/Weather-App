package fr.iut.kotlin.androidproject.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    private lateinit var weatherList : MutableList<WeatherData>

    private lateinit var btn_day1 : Button
    private lateinit var btn_day2 : Button
    private lateinit var btn_period0 : Button
    private lateinit var btn_period1 : Button
    private lateinit var btn_period2 : Button
    private lateinit var btn_period3 : Button

    private var day = 0
    private var period = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherList = WeatherSingleton.weatherList[day].dayList[period].periodList
        adapter = WeatherListAdapter(activity as Context, weatherList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_weather, container, false)

        btn_day1 = fragmentView.findViewById(R.id.weather_day1)
        btn_day2 = fragmentView.findViewById(R.id.weather_day2)
        btn_period0 = fragmentView.findViewById(R.id.weather_period0)
        btn_period1 = fragmentView.findViewById(R.id.weather_period1)
        btn_period2 = fragmentView.findViewById(R.id.weather_period2)
        btn_period3 = fragmentView.findViewById(R.id.weather_period3)

        btn_day1.isEnabled = false
        btn_day1.isClickable = false
        btn_period0.isEnabled = false
        btn_period0.isClickable = false

        btn_day1.setOnClickListener {
            lockButtonDay(btn_day1)
            day = 0
            setWeather()
        }
        btn_day2.setOnClickListener {
            lockButtonDay(btn_day2)
            day = 1
            setWeather()
        }
        btn_period0.setOnClickListener {
            lockButtonPeriod(btn_period0)
            period = 0
            setWeather()
        }
        btn_period1.setOnClickListener {
            lockButtonPeriod(btn_period1)
            period = 1
            setWeather()
        }
        btn_period2.setOnClickListener {
            lockButtonPeriod(btn_period2)
            period = 2
            setWeather()
        }
        btn_period3.setOnClickListener {
            lockButtonPeriod(btn_period3)
            period = 3
            setWeather()
        }

        tvCommune = fragmentView.findViewById(R.id.tv_commune)
        if (MyLocationSingleton.commune.isEmpty())
            tvCommune.visibility = View.GONE
        else
            tvCommune.text = MyLocationSingleton.commune

        lvWeatherInfo = fragmentView.findViewById(R.id.weather_list)
        lvWeatherInfo.adapter = adapter

        return fragmentView
    }

    private fun setWeather() {
        weatherList = WeatherSingleton.weatherList[day].dayList[period].periodList
        adapter.notifyDataSetChanged()
        for (i in 0 until WeatherSingleton.weatherList.size) {
            for (j in 0 until WeatherSingleton.weatherList[i].dayList.size) {
                Log.i("APPLOG", "day: " + i + " / period: " + j + " -> " + WeatherSingleton.weatherList[i].dayList[j].periodList.size)
            }
        }
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
}