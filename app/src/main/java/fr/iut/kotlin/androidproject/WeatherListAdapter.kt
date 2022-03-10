package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.io.Serializable

class WeatherListAdapter(val context: Context, val list: MutableList<WeatherData>): BaseAdapter() {

    private lateinit var date: TextView
    private lateinit var temp: TextView

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_weather_list, parent, false)

        date = view.findViewById(R.id.weather_date)
        temp = view.findViewById(R.id.weather_temperature)
        date.text = list[position].date
        temp.text = list[position].temp
        return view
    }

    fun addItem(contact: WeatherData) {
        list.add(contact)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

}

data class WeatherData(var date: String, var temp: String):Serializable