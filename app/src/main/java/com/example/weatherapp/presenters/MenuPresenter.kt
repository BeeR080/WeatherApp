package com.example.weatherapp.presenters

import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.model.GeoCodeModel
import com.example.weatherapp.business.repos.MenuRepository
import com.example.weatherapp.business.repos.SAVED
import com.example.weatherapp.view.MenuView

class MenuPresenter : BasePresenter<MenuView>() {
    private val repo = MenuRepository(ApiProvider())

    override fun enable() {
        repo.dataEmmiter.subscribe{
            viewState.setLoading(false)
            if(it.purpose == SAVED){
                Log.d("123321", "enable: SAVED ${it.data}")
                viewState.fillFavoriteList(it.data)
            }else{
                Log.d("123321", "enable: CURRENT ${it.data}")

                viewState.fillPredictionList(it.data)
            }
        }
    }

    fun searchFor(str: String){
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel){
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel){
        repo.add(data)
    }

    fun getFavoriteList() {
        repo.updateFavorite()
    }
}