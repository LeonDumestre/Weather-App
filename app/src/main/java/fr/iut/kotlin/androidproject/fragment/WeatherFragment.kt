package fr.iut.kotlin.androidproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.utils.WeatherSingleton

class WeatherFragment : Fragment() {

    private lateinit var fragmentView : View
    private lateinit var tvCommune: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var tvTemp: TextView
    private lateinit var tvMinTemp: TextView
    private lateinit var tvMaxTemp: TextView

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_weather, container, false)

        tvCommune = fragmentView.findViewById(R.id.weather_commune)
        ivIcon = fragmentView.findViewById(R.id.weather_image)
        tvTemp = fragmentView.findViewById(R.id.weather_temperature)
        tvMinTemp = fragmentView.findViewById(R.id.weather_min_temperature)
        tvMaxTemp = fragmentView.findViewById(R.id.weather_max_temperature)

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

        setWeather()

        btn_day1.setOnClickListener {
            lockButtonDay(btn_day1)
            day = 0
            btn_period3.visibility = View.VISIBLE
            setWeather()
        }
        btn_day2.setOnClickListener {
            lockButtonDay(btn_day2)
            day = 1
            btn_period3.visibility = View.GONE
            setWeather()
        }
        btn_period0.setOnClickListener {
            lockButtonPeriod(btn_period0)
            if (!btn_day1.isEnabled) {
                btn_day2.isEnabled = true
                btn_day2.isClickable = true
            }
            period = 0
            setWeather()
        }
        btn_period1.setOnClickListener {
            lockButtonPeriod(btn_period1)
            if (!btn_day1.isEnabled) {
                btn_day2.isEnabled = true
                btn_day2.isClickable = true
            }
            period = 1
            setWeather()
        }
        btn_period2.setOnClickListener {
            lockButtonPeriod(btn_period2)
            if (!btn_day1.isEnabled) {
                btn_day2.isEnabled = true
                btn_day2.isClickable = true
            }
            period = 2
            setWeather()
        }
        btn_period3.setOnClickListener {
            lockButtonPeriod(btn_period3)
            btn_day2.isEnabled = false
            btn_day2.isClickable = false
            period = 3
            setWeather()
        }

        return fragmentView
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather() {
        val weatherData = WeatherSingleton.weatherList[day].dayList[period].periodList[0]
        //tvCommune.text = weatherData.communeLocation.commune
        ivIcon.setImageResource(weatherData.icon)
        tvTemp.text = weatherData.temp.toString() + "°C"
        tvMinTemp.text = "Min: " + weatherData.minTemp.toString() + "°C"
        tvMaxTemp.text = "Max: " + weatherData.maxTemp.toString() + "°C"
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