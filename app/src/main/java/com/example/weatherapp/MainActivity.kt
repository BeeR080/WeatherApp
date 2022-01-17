package com.example.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DealyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.view.*
import com.example.weatherapp.view.adapters.MainDealyListAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter



const val TAG = "GEO"

class MainActivity : MvpAppCompatActivity(), MainView {

    lateinit var  binding: ActivityMainBinding

    private val geoService by lazy{ LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy{ initLocationRequest() }
    private lateinit var mLocation: Location

    private val  mainPresenter by moxyPresenter{ MainPresenter() }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.rvMainHourlyList.apply {
          adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false)
            setHasFixedSize(true)
        }
        binding.rvMainDailyList.apply {
          adapter = MainDealyListAdapter()
          layoutManager = LinearLayoutManager(context,
              LinearLayoutManager.VERTICAL,
              false)
          setHasFixedSize(true)
      }


        geoService.requestLocationUpdates(
            locationRequest,
            geoCallback,
            null

        )

        mainPresenter.enable()

    }

    //---------Moxy Code--------
    override fun displayLocation(data: String) {
        binding.tvMainCity.text = data
    }

    override fun displayCurrentData(data: WeatherDataModel) {

        data.apply {
            binding.tvMainDate.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            binding.imageSunIcon.setImageResource(current.weather[0].icon.provideIcon())
            binding.mainTemp.text = StringBuilder()
                .append(current.temp.toDegree())
                .append("°").toString()
            daily[0].temp.apply {
                binding.mainMinTemp.text = min.toDegree()
                binding.mainMaxTemp.text = max.toDegree()
            }
            binding.mainWeatherStatus.text = current.weather[0].description

            binding.imgWeather.setImageResource(current.weather[0].icon.provideImage())
            binding.mainPressureMuTv.text = StringBuilder()
                .append(current.pressure.toString())
                .append(" hPa").toString()
            binding.mainHumidityMuTv.text = StringBuilder()
                .append(current.humidity.toString())
                .append(" %").toString()
            binding.mainWindSpeedTv.text = StringBuilder()
                .append(current.wind_speed.toString())
                .append(" m/s").toString()
            binding.mainSunriseTv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            binding.mainSunsetTv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)

        }

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (binding.rvMainHourlyList.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDealyData(data: List<DealyWeatherModel>) {
        (binding.rvMainDailyList.adapter as MainDealyListAdapter).updateData(data)
    }

    override fun displayError(error: Throwable) {

    }

    override fun setLoading(flag: Boolean) {

    }
    //---------Moxy Code--------

    //--------------- Location Code ----------------

private fun initLocationRequest(): LocationRequest {
    val request = LocationRequest.create()
    return request.apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

}
    private val geoCallback = object : LocationCallback(){
        override fun onLocationResult(geo: LocationResult) {
           for (location in geo.locations){
               mLocation = location
               mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
               Log.d(TAG,"lat: ${location.latitude} ; lon: ${location.longitude}")
           }
        }
    }






    //--------------- Location Code ----------------


}