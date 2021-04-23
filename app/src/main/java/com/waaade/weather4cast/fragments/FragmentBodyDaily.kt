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
import com.waaade.weather4cast.adapters.DailyAdapter

class FragmentBodyDaily : Fragment() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_body_daily,container,false)

        recyclerView = view.findViewById(R.id.daily_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(MainActivity(),1)
        recyclerView.adapter = DailyAdapter(MainActivity.WeatherData.daily)

        return view
    }
}