package com.example.weatherapp.business


import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
   @GET("data/2.5/onecall?")

   fun getWeatherForecast(
       @Query("lat") lat: String,
       @Query("lon") lon: String,
       @Query("exclude") exclude: String = "minutely,alerts",
       @Query("appid") appid: String = "3d51362ac38a4e3cefe42bb924f20573",
       @Query("lang") lang: String = "en"
   ): Observable<WeatherDataModel>

}