package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DealyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.view.*
import com.example.weatherapp.view.adapters.MainDealyListAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.Exception
import kotlin.math.roundToInt


const val TAG = "GEO"
const val COORDINATES = "Coordinates"

class MainActivity : MvpAppCompatActivity(), MainView {

    lateinit var  binding: ActivityMainBinding

    private val  mainPresenter by moxyPresenter{ MainPresenter() }


    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private lateinit var mLocation: Location




    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initBottomSheets()
        initSwipeRefrash()
        binding.refresh.isRefreshing = true

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



        mainPresenter.enable()

        binding.btnMainMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        if (!intent.hasExtra(COORDINATES)) {
            checkGeoAvailability()
            Log.d(TAG, "onCreate: ")
            getGeo()
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

    }
    private fun initViews() {
        binding.tvMainCity.text = "Moscow"
        binding.tvMainDate.text = "1 april"
        binding.imageSunIcon.setImageResource(R.drawable.ic_sun)
        binding.mainTemp.text = "25\u00B0"
        binding.mainMinTemp.text = "19"
        binding.mainMaxTemp.text = "28"
        binding.imgWeather.setImageResource(R.mipmap.oblachko)
        binding.mainPressureMuTv.text = "1023 hPa"
        binding.mainHumidityMuTv.text = "88 %"
        binding.mainWindSpeedTv.text = "5 m/s"
        binding.mainSunriseTv.text = "4:30"
        binding.mainSunsetTv.text = "22:43"
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
            binding.refresh.isRefreshing = false

        }

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (binding.rvMainHourlyList.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDealyData(data: List<DealyWeatherModel>) {
        (binding.rvMainDailyList.adapter as MainDealyListAdapter).updateData(data)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
    }

    override fun setLoading(flag: Boolean) {
        binding.refresh.isRefreshing = flag
    }
    //---------Moxy Code--------

    //--------------- Location Code ----------------




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


    private fun initBottomSheets() {
        binding.bottomSheetsContainer.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        binding.bottomSheetsContainer.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.5).roundToInt())
    }

    private fun initSwipeRefrash() {
        binding.refresh.apply {
            setColorSchemeResources(R.color.purple_700)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }

        }
    }
    @SuppressLint("MissingPermission")
    private fun getGeo(){
        geoService
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                Log.d(TAG, "getGeo: ")
                if(it!=null){
                    mLocation = it
                    mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                }else{
                    displayError(Exception("Geodata is not available"))
                }
                Log.d(TAG, "requestGeo: $it")
            }
    }
    private fun checkGeoAvailability() {
        Log.d(TAG, "checkGeoAvailability: ")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }


}