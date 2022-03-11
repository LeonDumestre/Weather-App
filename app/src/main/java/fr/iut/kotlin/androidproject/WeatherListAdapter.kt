package fr.iut.kotlin.androidproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.io.Serializable


class WeatherListAdapter(val context: Context, val list: MutableList<WeatherData>): BaseAdapter() {

    private lateinit var tvDate: TextView
    private lateinit var tvTemp: TextView

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_weather_list, parent, false)

        tvDate = view.findViewById(R.id.weather_date)
        tvTemp = view.findViewById(R.id.weather_temperature)

        tvDate.text = list[position].date
        tvTemp.text = list[position].temp + "°C"
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

fun getActivity(context: Context?): Activity? {
    if (context == null) return null
    if (context is Activity) return context
    return if (context is ContextWrapper) getActivity(context.baseContext) else null
}

data class WeatherData(var date: String, var temp: String):Serializable