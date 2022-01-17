package com.example.weatherapp.presenters

import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.repos.MainRepository
import com.example.weatherapp.view.MainView

class MainPresenter: BasePresenter<MainView>() {
private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmmiter.subscribe{response ->
            Log.d("MAINREPO"," Presenetr enable(:$response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDealyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let{viewState.displayError(response.error)}
        }

    }

    fun refresh(lat: String, lon:String){
        viewState.setLoading(true)
        repo.reloadData(lat, lon)
    }
}