package com.trunnghieu.tplogisticsapplication.utils.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger
import kotlinx.coroutines.*
import okio.IOException
import java.net.InetSocketAddress
import java.net.Socket

object NetworkHelper {

    private fun isConnected(appContext: Context): Boolean {
        var result = false
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.activeNetwork ?: return false
            val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    /**
     * Check if the internet is really working by ping to specific host
     */
    fun isOnline(
        appContext: Context,
        autoRefresh: Boolean = true,
        listener: NetworkConnectionListener
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isConnected(appContext)) {
                listener.onNetworkConnectionChanges(false)

                if (autoRefresh) {
                    // Check connection again
                    delay(5000)
                    isOnline(appContext, true, listener)
                }
                return@launch
            }

            try {

                withContext(Dispatchers.IO) {
                    val timeOutMs = 3000
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)
//                    val socketAddress = InetSocketAddress("10.125.200.253", 3128)
                    socket.run {
                        connect(socketAddress, timeOutMs)
                        close()
                    }
                }
                listener.onNetworkConnectionChanges(true)
                return@launch

            } catch (ignore: IOException) {
                CustomLogger.e("Connection error")
            }

            listener.onNetworkConnectionChanges(false)

            if (autoRefresh) {
                // Check connection again
                CustomLogger.e("Check connection again")
                delay(5000)
                isOnline(appContext, true, listener)
            }
        }
    }

    interface NetworkConnectionListener {
        fun onNetworkConnectionChanges(isConnected: Boolean)
    }
}