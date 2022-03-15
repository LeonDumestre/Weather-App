package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import fr.iut.kotlin.androidproject.data.WeatherData
import java.io.Serializable


class WeatherListAdapter(val context: Context, val list: MutableList<WeatherData>): BaseAdapter() {

    private lateinit var tvDate: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvMinTemp: TextView
    private lateinit var tvMaxTemp: TextView
    private lateinit var ivImage: ImageView

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addItem(contact: WeatherData) {
        list.add(contact)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_weather_list, parent, false)

        ivImage = view.findViewById(R.id.weather_image)
        tvDate = view.findViewById(R.id.weather_date)
        tvTemp = view.findViewById(R.id.weather_temperature)
        tvMinTemp = view.findViewById(R.id.weather_min_temperature)
        tvMaxTemp = view.findViewById(R.id.weather_max_temperature)

        ivImage.setImageResource(list[position].icon)
        tvDate.text = list[position].date
        tvTemp.text = list[position].temp.toString() + "°C"
        tvMinTemp.text = "Min : " + list[position].minTemp + "°C"
        tvMaxTemp.text = "Max : " + list[position].maxTemp + "°C"

        return view
    }

}