package com.example.weatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class, GeoCodeEntity::class], exportSchema = false, version = 1)

abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDAO

    abstract fun getGeoCodeDao(): GeoCodeDao
}