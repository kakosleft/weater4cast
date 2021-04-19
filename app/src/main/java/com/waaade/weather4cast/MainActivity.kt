//в этом проекте для хранения api ключа используется
//файл под названием API_KEY, если вы хотите запустить
//этот проект то создайте API_KEY.kt и создайте в нем
//публичную переменную key. Переменной key присвойте
//значение вашего OpenWeather one api ключа

package com.waaade.weather4cast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val apiKey = API_KEY().key
    val URL =
        "https://api.openweathermap.org/data/2.5/onecall?lat" +
                "=42.4714808&lon=59.53379&lang=ru&units=metric&appid="+apiKey
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getWeather()

//        var qwe = TEST("vasya",2,false,"qwe")
//        var gson = Gson()
//        val jsonString = gson.toJson(TEST("vasya",2,false,"qwe"))
//        println("---------"+jsonString)


    }


    private fun getWeather() {

        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
//                val txt = (JSONObject(json).getJSONObject("value").get("joke")).toString()
//                println(json)
                Weather().parseCurrent(JSONObject(json).getJSONObject("current"))

            }
        })

    }


}