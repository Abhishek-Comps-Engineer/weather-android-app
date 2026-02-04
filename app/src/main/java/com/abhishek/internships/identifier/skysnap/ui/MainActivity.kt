package com.abhishek.internships.identifier.skysnap.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.emoji2.text.EmojiCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.internships.identifier.skysnap.R
import com.abhishek.internships.identifier.skysnap.adpater.CityAdapter
import com.abhishek.internships.identifier.skysnap.databinding.ActivityMainBinding
import com.abhishek.internships.identifier.skysnap.util.Constant
import com.abhishek.internships.identifier.skysnap.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val viewModel by viewModels<MainViewModel>()
    private val adapter = CityAdapter()
    private val TAG = "MainActivity"

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)
        applyInsets()

        Log.d(TAG, "EmojiCompat initialized = ${EmojiCompat.isConfigured()}")

        setupRecyclerView()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Attach observer BEFORE requesting location
        observeCityData()

        // Request location if enabled & permission granted
        if (!isLocationEnable()) {
            openLocationSettings()
        } else if (hasLocationPermission()) {
            requestLocationData()
        } else {
            requestPermission()
        }
    }

    private fun setupRecyclerView() {
        binding.cityItemRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.cityItemRecyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeCityData() {
        viewModel.weatherData.observe(this) { weather ->
            if (weather != null) {
                Log.d(TAG, "Weather received: $weather")
                binding.windSpeed.text = weather.current?.wind_speed_10m.toString()
                binding.temperatureTxt.text = buildString {
                    append(weather.current?.temperature_2m.toString())
                    append(weather.current_units?.temperature_2m)
                }
                binding.humidity.text = buildString {
                    append(weather.current?.relative_humidity_2m.toString())
                    append(weather.current_units?.relative_humidity_2m)
                }
                binding.cloudCover.text = buildString{
                    append(weather.current?.surface_pressure.toString())
                    append(weather.current_units?.surface_pressure)
                }

                binding.tvDate.text = viewModel.formatDate(weather.current?.time.toString())
            } else {
                Log.d(TAG, "No current weather data")
            }
        }
    }

    private fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.screen_padding)
            v.setPadding(
                systemBars.left + padding,
                systemBars.top + padding,
                systemBars.right + padding,
                systemBars.bottom + padding
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
        if (requestCode == Constant.REQUEST_LOCATION_CONSTANT
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            requestLocationData()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun requestLocationData() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).setMaxUpdates(1).build()

        mFusedLocationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (location != null) {
                        Log.d(TAG, "Location received: $location")
                        mFusedLocationClient.removeLocationUpdates(this)
                        if (Constant.isNetworkAvailable(applicationContext)) {
                            viewModel.getLocationWeatherDetails(
                                location.latitude,
                                location.longitude
                            )
                        } else {
                            Log.d(TAG, "Network unavailable, cannot fetch weather")
                        }
                    } else {
                        Log.d(TAG, "Location result is null")
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun isLocationEnable(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            showRequestDialog()
        } else {
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

    private fun showRequestDialog() {
        AlertDialog.Builder(this)
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (activity: ActivityNotFoundException) {
                    activity.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.cancel()
            }
            .setTitle("Location Permission Needed")
            .setMessage("This app needs the Location permission, please accept to use location functionality")
            .show()
    }
}
