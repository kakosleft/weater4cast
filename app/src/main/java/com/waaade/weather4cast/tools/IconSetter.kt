package com.waaade.weather4cast.tools

import com.waaade.weather4cast.R

class IconSetter {

     fun setWeather(icon: String): Int {
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

    fun setWindDegIcon(deg: String): Int {
        var i: Int
        val direction = deg.toInt()

        if (direction >= 340 || direction in 0..23) {
            i = R.drawable.north
        }else if(direction in 290..340){
            i = R.drawable.north_west
        }else if(direction in 245..290){
            i = R.drawable.west
        }else if(direction in 200..245){
            i = R.drawable.south_west
        }else if(direction in 155..200){
            i = R.drawable.south
        }else if(direction in 110..155){
            i = R.drawable.south_east
        }else if(direction in 68..110){
            i = R.drawable.east
        }else{
            i = R.drawable.north_east
        }
        return i
    }

}