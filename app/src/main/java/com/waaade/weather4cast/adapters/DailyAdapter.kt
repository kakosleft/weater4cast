package com.waaade.weather4cast.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waaade.weather4cast.R
import com.waaade.weather4cast.models.DailyWeather
import com.waaade.weather4cast.tools.IconSetter

class DailyAdapter(private val dataSet: MutableList<DailyWeather>) :
        RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val temp_min: TextView
        val temp_max: TextView
        val temp_day: TextView
        val temp_morn: TextView
        val temp_eve: TextView
        val temp_night: TextView

        val feels_like_morn: TextView
        val feels_like_day: TextView
        val feels_like_eve: TextView
        val feels_like_night: TextView

        val ico: ImageView
        val dt: TextView
        val description: TextView


        init {
            description = view.findViewById(R.id.daily_description)
            dt = view.findViewById(R.id.daily_dt)
            temp_min = view.findViewById(R.id.daily_temp_min)
            temp_max = view.findViewById(R.id.daily_temp_max)
            temp_morn = view.findViewById(R.id.daily_temp_morn)
            temp_day = view.findViewById(R.id.daily_temp_day)
            temp_eve = view.findViewById(R.id.daily_temp_eve)
            temp_night = view.findViewById(R.id.daily_temp_night)

            feels_like_day = view.findViewById(R.id.daily_feels_like_day)
            feels_like_morn = view.findViewById(R.id.daily_feels_like_morn)
            feels_like_eve = view.findViewById(R.id.daily_feels_like_eve)
            feels_like_night = view.findViewById(R.id.daily_feels_like_night)

            ico = view.findViewById(R.id.daily_icon)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.daily_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.description.text = dataSet[position].description
        viewHolder.dt.text = dataSet[position].dt
        viewHolder.temp_min.text = dataSet[position].temp.min
        viewHolder.temp_max.text = dataSet[position].temp.max
        viewHolder.temp_morn.text = dataSet[position].temp.morn
        viewHolder.temp_day.text = dataSet[position].temp.day
        viewHolder.temp_eve.text = dataSet[position].temp.eve
        viewHolder.temp_night.text = dataSet[position].temp.night

        viewHolder.feels_like_day.text = dataSet[position].feels_like.day
        viewHolder.feels_like_morn.text = dataSet[position].feels_like.morn
        viewHolder.feels_like_eve.text = dataSet[position].feels_like.eve
        viewHolder.feels_like_night.text = dataSet[position].feels_like.night

        viewHolder.ico.setImageResource(IconSetter().setWeather(dataSet[position].icon))

    }

    override fun getItemCount() = dataSet.size

}