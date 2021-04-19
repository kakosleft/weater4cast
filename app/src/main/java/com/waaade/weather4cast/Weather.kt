package com.waaade.weather4cast

import org.json.JSONObject

class Weather {

    fun parseCurrent(json: JSONObject){
        println("sunrise"+json.get("sunrise").toString())
        println("sunset"+json.get("sunset").toString())
        println("temp"+json.get("temp").toString())
        println("feels_like"+json.get("feels_like").toString())
        println("pressure"+json.get("pressure").toString())
        println("humidity"+json.get("humidity").toString())
        println("dew_point"+json.get("dew_point").toString())
        println("wind_speed"+json.get("wind_speed").toString())
        println("wind_deg"+json.get("wind_deg").toString())
    }

    fun parseMinutely(json: JSONObject){

    }

    fun parseHourly(json: JSONObject){

    }

    fun parseDaily(json: JSONObject){

    }


}