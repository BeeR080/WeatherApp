package com.example.weatherapp.view.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.databinding.WeatherlistHourlyMainBinding
import com.example.weatherapp.view.*
import com.google.android.material.textview.MaterialTextView


class MainHourlyListAdapter: BaseAdapter<HourlyWeatherModel>() {

    lateinit var binding: WeatherlistHourlyMainBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
     val inflater = LayoutInflater.from(parent.context)
        binding = WeatherlistHourlyMainBinding.inflate(inflater,parent,false)
        return  HourlyViewHolder(binding)

    }

    inner class HourlyViewHolder(binding: WeatherlistHourlyMainBinding) : BaseViewHolder(binding.root){
         val time: MaterialTextView = binding.weatherlistHourlyTimeTv
         val temperature = binding.weatherlistHourlyTempTv
         val popRate: MaterialTextView = binding.weatherlistHourlyPopTv
         val icon: ImageView = binding.weatherlistHourlyConditionIcon

init{

}

        override fun bindView(position: Int) {
            mData[position].apply {
                time.text = dt.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
                temperature.text = StringBuilder()
                    .append(temp.toDegree())
                    .append("°").toString()
                popRate.text = pop.toPersentString(" %")
                icon.setImageResource(weather[0].icon.provideIcon())

            }



        }

    }

}