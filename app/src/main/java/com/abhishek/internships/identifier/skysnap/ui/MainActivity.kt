package com.abhishek.internships.identifier.skysnap.ui

import android.Manifest
import android.R.attr.resource
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abhishek.internships.identifier.skysnap.R
import com.abhishek.internships.identifier.skysnap.databinding.ActivityMainBinding
import com.abhishek.internships.identifier.skysnap.retrofitcall.ServiceApi
import com.abhishek.internships.identifier.skysnap.util.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)
        applyInsets()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if ( !isLocationEnable()){
//            If location is not enabled then show alert dialog
            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Location is enabled", Toast.LENGTH_SHORT).show()
//            If location is enabled then start the service
            requestPermission()  // always ask permission -> problem
        }

    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.screen_padding)

            v.setPadding(
                systemBars.left +padding,
                systemBars.top +padding,
                systemBars.right +padding,
                systemBars.bottom +padding
            )
            insets
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constant.REQUEST_LOCATION_CONSTANT
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            requestLocationData()
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestLocationData() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        mFusedLocationClient.requestLocationUpdates(locationRequest, object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Toast.makeText(this@MainActivity, "Location $locationResult", Toast.LENGTH_SHORT).show()
                getLocationWeatherDetails()
            }
        }, Looper.myLooper())
    }


//    ✅ It tells you whether the device’s location services are turned on not
    private fun isLocationEnable(): Boolean{
        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


//    The user has granted location permission to YOUR APP for access location
    private fun requestPermission() {
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // User denied previously → then show explanation dialog
            showRequestDialog()
        } else {
            // Ask for permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constant.REQUEST_LOCATION_CONSTANT
            )
        }
    }

    private fun showRequestDialog(){
        AlertDialog.Builder(this)
            .setPositiveButton("GO TO SETTINGS"){ _,_ ->
                try{
                    val intent  = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (activity: ActivityNotFoundException){
                    activity.printStackTrace()
                }catch(e: Exception){
                    e.printStackTrace()
                }
            }.setNegativeButton("CANCEL"){dialog,_ ->
                dialog.cancel()
            }.setTitle("Location Permission Needed")
            .setMessage("This app needs the Location permission, please accept to use location functionality")
            .show()
    }

    private fun getLocationWeatherDetails(){
        if (Constant.isNetworkAvailable(this)){
            Toast.makeText(this, "Network Available", Toast.LENGTH_SHORT).show()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val serviceApi = retrofit.create(ServiceApi::class.java)






        }else{
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show()
        }
    }
}