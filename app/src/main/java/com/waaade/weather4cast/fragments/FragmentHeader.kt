package com.waaade.weather4cast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.waaade.weather4cast.MainActivity
import com.waaade.weather4cast.R

class FragmentHeader() : Fragment() {

    private lateinit var tempTextView: TextView
    private lateinit var windSpeedTextView: TextView
    private lateinit var windDegImagetView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var humidityTextView: TextView
    private lateinit var visibilityTextView: TextView
    private lateinit var feelsLikeTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_header,container,false)

        tempTextView = view.findViewById(R.id.head_temperature)
        windSpeedTextView = view.findViewById(R.id.head_wind_speed)
        descriptionTextView = view.findViewById(R.id.head_description)
        humidityTextView = view.findViewById(R.id.head_humidity)
        visibilityTextView = view.findViewById(R.id.head_visibility)
        feelsLikeTextView = view.findViewById(R.id.head_feels_like_temp)

        windDegImagetView = view.findViewById(R.id.head_wind_deg)
        weatherIcon = view.findViewById(R.id.head_weather_icon)

        val currentWeather = MainActivity.WeatherData.current

        tempTextView.text = currentWeather.temp
        windSpeedTextView.text = currentWeather.wind_speed
        descriptionTextView.text = currentWeather.description
        humidityTextView.text = currentWeather.humidity
        visibilityTextView.text = currentWeather.visibility
        feelsLikeTextView.text = currentWeather.feels_like
        weatherIcon.setImageResource(setIcom(currentWeather.icon))

        return view
    }

    private fun setIcom(icon: String): Int {
        var i: Int
        when(icon){
            "01d"-> i = R.drawable.ico01d
            "02d"-> i = R.drawable.ico02d
            "03d"-> i = R.drawable.ico03d
            "04d"-> i = R.drawable.ico04d
            "09d"-> i = R.drawable.ico09d
            "10d"-> i = R.drawable.ico10d
            "11d"-> i = R.drawable.ico11d
            "13d"-> i = R.drawable.ico13d
            "50d"-> i = R.drawable.ico50d
            "01n"-> i = R.drawable.ico01n
            "02n"-> i = R.drawable.ico02n
            "03n"-> i = R.drawable.ico03n
            "04n"-> i = R.drawable.ico04n
            "09n"-> i = R.drawable.ico09n
            "10n"-> i = R.drawable.ico10n
            "11n"-> i = R.drawable.ico11n
            "13n"-> i = R.drawable.ico13n
            else -> i = R.drawable.ico50n
        }
        return i
    }



}