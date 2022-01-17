package com.example.weatherapp.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.WeatherlistDealyMainBinding
import com.example.weatherapp.databinding.WeatherlistHourlyMainBinding

abstract class BaseAdapter<D>: RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    //Данные с сервера лежат здесь
    private val _mData by lazy{ mutableListOf<D>()}
    protected val mData: List<D> = _mData


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount() = mData.size

fun updateData(data: List<D>){
    if(_mData.isEmpty() && (data.isNotEmpty())){
        _mData.addAll(data)
    }else{
        _mData.clear()
        _mData.addAll(data)
    }
    notifyDataSetChanged()
}

    abstract class BaseViewHolder(binding: View): RecyclerView.ViewHolder(binding){

        abstract fun  bindView(position: Int)
    }
}



