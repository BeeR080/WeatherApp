package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.example.weatherapp.business.model.DealyWeatherModel
import com.example.weatherapp.databinding.WeatherlistDealyMainBinding
import com.example.weatherapp.view.*
import com.google.android.material.textview.MaterialTextView

class MainDealyListAdapter: BaseAdapter<DealyWeatherModel>() {




    lateinit var binding: WeatherlistDealyMainBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = WeatherlistDealyMainBinding.inflate(inflater,parent,false)

        return  DealyViewHolder(binding)
    }

    inner class DealyViewHolder(binding: WeatherlistDealyMainBinding) : BaseViewHolder(binding.root){
        val day: MaterialTextView = binding.weatherlistDealyDateTv
        val condition: MaterialTextView = binding.weatherlistDealyCondititonTv
        val maxTemperature: MaterialTextView = binding.weatherlistDealyMaxTempTv
        val minTemperature: MaterialTextView = binding.weatherlistDealyMinTempTv
        val iconCondition: ImageView = binding.weatherlistDealyConditionIcon


        override fun bindView(position: Int) {
            mData[position].apply{
                day.text = dt.toDateFormatOf(DAY_WEEK_NAME_LONG)
                iconCondition.setImageResource(weather[0].icon.provideIcon())
                condition.text = pop.toPersentString(" %")
                maxTemperature.text = StringBuilder()
                    .append(temp.max.toDegree())
                    .append("°").toString()
                minTemperature.text = StringBuilder()
                    .append(temp.min.toDegree())
                    .append("°").toString()

            }


        }


    }



}