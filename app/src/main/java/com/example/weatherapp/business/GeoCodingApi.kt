package com.example.weatherapp.business

import com.example.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/reverse?")
    fun getCityByCooed(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") id: String = "3d51362ac38a4e3cefe42bb924f20573"

    ): Observable<List<GeoCodeModel>>
}