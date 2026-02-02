package com.abhishek.internships.identifier.skysnap.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

object Constant {
    const val REQUEST_LOCATION_CONSTANT = 6223

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

//        if the device's API is greater than or equal to 23
        if (Build.VERSION.SDK >= Build.VERSION_CODES.M.toString()){
            val network = connectivityManager.activeNetwork ?: return false
        }
    }
}