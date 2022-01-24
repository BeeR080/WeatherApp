package com.example.weatherapp

import android.app.Application
import android.content.Intent
import androidx.room.Room
import com.example.weatherapp.business.room.WeatherDataBase

const val APP_SETTINGS = " App settings"
const val IS_STARTED_UP = " Is started up"

class App: Application() {

   companion object{
       lateinit var db :WeatherDataBase
   }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, WeatherDataBase::class.java, "WeatherDB" )
            .fallbackToDestructiveMigration()
            .build()

        val pref = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        val flag = pref.contains(IS_STARTED_UP)

        if(!flag){
            //Записываем данные о флаге в константу
            val editor = pref.edit()
            editor.putBoolean(IS_STARTED_UP,true)
            editor.apply()

            val intent = Intent(this,InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}