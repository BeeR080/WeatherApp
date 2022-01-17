package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.weatherapp.databinding.ActivityInitialBinding
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder



const val GEO_LOCATION_REQUEST_COD_SUCCESS = 1

class InitialActivity : AppCompatActivity() {

    lateinit var  binding: ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chekPermission()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == GEO_LOCATION_REQUEST_COD_SUCCESS && permissions.isNotEmpty()){
            val intent = Intent(this, MainActivity::class.java)

            // Чтобы не вернуться обратно в InitialActivity. Удаляется из стека обратного вызова
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun chekPermission(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            MaterialAlertDialogBuilder(this)
                .setTitle("Для работы приложения необходимо получить гео данные")
                .setMessage("Пожалуйста, разрешите доступ к вашим гео данным для продолжения работы приложения")
                .setPositiveButton("Ок"){_,_->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        GEO_LOCATION_REQUEST_COD_SUCCESS)
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        GEO_LOCATION_REQUEST_COD_SUCCESS)
                }
                .setNegativeButton("Отмена"){ dialog,_->dialog.dismiss()}
                .create()
                .show()


        }
    }




}