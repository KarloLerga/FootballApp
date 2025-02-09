package org.unizd.rma.prezime.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            val isAvailable = activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

            Log.d("NetworkUtils", "Network available: $isAvailable")
            isAvailable
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            val isAvailable = networkInfo != null && networkInfo.isConnected

            Log.d("NetworkUtils", "Legacy network available: $isAvailable")
            isAvailable
        }
    }
}
