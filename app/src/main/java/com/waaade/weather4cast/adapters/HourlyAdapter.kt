package com.waaade.weather4cast.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waaade.weather4cast.R
import com.waaade.weather4cast.models.HourlyWeather
import com.waaade.weather4cast.tools.IconSetter

class HourlyAdapter(private val dataSet: MutableList<HourlyWeather>) :
        RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView
        val dt: TextView
        val temp: TextView
        val feelsLike: TextView
        val icon: ImageView
        val description: TextView
        val windSpeed: TextView
        val windDeg: ImageView


        init {
            day = view.findViewById(R.id.hourly_is_today)
            dt = view.findViewById(R.id.hourly_dt)
            temp = view.findViewById(R.id.hourly_temp_text_view)
            feelsLike = view.findViewById(R.id.hourly_feels_like_text_view)
            icon = view.findViewById(R.id.hourly_icon)
            description = view.findViewById(R.id.hourly_description)
            windSpeed = view.findViewById(R.id.hourly_wind_speed_text_view)
            windDeg = view.findViewById(R.id.hourly_wind_deg)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.hourly_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.day.text = dataSet[position].day
        viewHolder.dt.text = dataSet[position].dt
        viewHolder.temp.text = dataSet[position].temp
        viewHolder.feelsLike.text = dataSet[position].feels_like
        viewHolder.description.text = dataSet[position].description
        viewHolder.windSpeed.text = dataSet[position].wind_speed
        viewHolder.windDeg.setImageResource(IconSetter().setWindDegIcon(dataSet[position].wind_deg))
        viewHolder.icon.setImageResource(IconSetter().setWeather(dataSet[position].icon))
    }

    override fun getItemCount() = dataSet.size

}
