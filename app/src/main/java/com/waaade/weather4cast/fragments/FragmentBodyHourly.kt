package com.waaade.weather4cast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waaade.weather4cast.MainActivity
import com.waaade.weather4cast.R
import com.waaade.weather4cast.adapters.HourlyAdapter

class FragmentBodyHourly : Fragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_body_hourly,container,false)

        recyclerView = view.findViewById(R.id.hourly_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(MainActivity(),1)
        recyclerView.adapter = HourlyAdapter(MainActivity.WeatherData.hourly)

        return view
    }
}