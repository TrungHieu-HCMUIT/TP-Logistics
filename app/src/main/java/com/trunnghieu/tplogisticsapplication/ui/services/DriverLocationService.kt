package com.trunnghieu.tplogisticsapplication.ui.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.trunnghieu.tplogisticsapplication.TPLogisticsApp
import com.trunnghieu.tplogisticsapplication.ui.screens.intro.IntroActivity
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger
import com.trunnghieu.tplogisticsapplication.utils.NotificationUtils
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.driver.DriverRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback
import com.trunnghieu.tplogisticsapplication.data.repository.remote.common.CommonRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.common.driver_location_update.UpdateDriverLocationDTO
import kotlinx.coroutines.*
import okhttp3.ResponseBody

class DriverLocationService : Service() {

    companion object {
        var lastKnownLocationOfDriver: Location? = null
    }

    private lateinit var context: Context

    // Location
    private var isLocationRequested = false
    private var driverLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    // Tracking job
    private var trackingLocationJob: Job? = null

    // Repo
    private val commonRepo = CommonRepo()

    override fun onCreate() {
        super.onCreate()
        context = this
        showForegroundNotification()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

//    override fun getLocationConfiguration(): LocationConfiguration {
//        return LocationHelper.get().createConfigUsingGoogleService()
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(TPLogisticsConst.LOCATION.REQUEST_GOOGLE_LOCATION_SERVICE_INTERVAL)

        if (!isLocationRequested) {
            isLocationRequested = true
            requestLocationUpdate()
//            getLocation()
        }

        trackingLocation()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTrackingLocation()
    }

//    override fun onLocationChanged(location: Location) {
//        CustomLogger.e("User's background location changed -->> $location")
//        driverLocation = location
//    }

//    override fun onLocationFailed(type: Int) {
//        CustomLogger.e("onLocationFailed: $type")
//        FAnalytics.logEvent(
//            FAnalytics.EVENT_UPDATE_DRIVER_LOCATION,
//            FAnalytics.Analytic(FAnalytics.FIELD_LOCATION_FAIL, "Fail type: $type")
//        )
//        driverLocation = null
//
//        if (type == FailType.PERMISSION_DENIED) {
//            stopTrackingLocation()
//        }
//    }

//    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//    }
//
//    override fun onProviderEnabled(provider: String) {
//
//    }
//
//    override fun onProviderDisabled(provider: String) {
//        CustomLogger.e("onProviderDisabled: $provider")
//        FAnalytics.logEvent(
//            FAnalytics.EVENT_UPDATE_DRIVER_LOCATION,
//            FAnalytics.Analytic(FAnalytics.FIELD_LOCATION_FAIL, "$provider is disabled")
//        )
//        driverLocation = null
//    }

    private fun trackingLocation() {
        trackingLocationJob = CoroutineScope(Dispatchers.Main).launch {
            sendDriverLocation()
            withContext(Dispatchers.IO) {
                delay(TPLogisticsConst.LOCATION.TRACKING_LOCATION_PERIOD)
                trackingLocation()
            }
        }
        trackingLocationJob!!.start()
    }

    private fun stopTrackingLocation() {
        CustomLogger.e("Stop tracking location")
        lastKnownLocationOfDriver = null

        // Stop tracking location
//        locationManager.cancel()
        fusedLocationClient.removeLocationUpdates(locationCallback)

        trackingLocationJob?.cancel()
        trackingLocationJob = null

        // Also stop foreground notification
        stopForeground(true)

        stopSelf()
    }

    private fun showForegroundNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        // Only show foreground notification from Android 8
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, IntroActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            },
            NotificationUtils.pendingIntentFlag
        )
        val foregroundNotification = NotificationUtils.buildNotification(
            context,
            TPLogisticsApp.CHANNEL_ID,
            TPLogisticsApp.CHANNEL_NAME,
            TPLogisticsConst.NOTIFICATION_ICON,
            context.getString(R.string.foreground_notification_title),
            context.getString(R.string.foreground_notification_content),
            NotificationCompat.PRIORITY_LOW,
            1,
            pendingIntent
        )
        startForeground(System.currentTimeMillis().toInt(), foregroundNotification)
    }

    @SuppressLint("MissingPermission")
    private fun sendDriverLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
            CustomLogger.e("Get last location: $lastLocation")
            driverLocation = lastLocation

            validateLocation {
                val latitude = driverLocation?.latitude
                val longitude = driverLocation?.longitude
                CustomLogger.e("Driver's location >>> $latitude, $longitude")

                val bodyRequest = UpdateDriverLocationDTO(
                    latitude = latitude,
                    longitude = longitude
                )
                commonRepo.updateDriverLocation(
                    bodyRequest,
                    object : BaseRepoCallback<ResponseBody> {
                        override fun apiResponse(data: ResponseBody) {}

                        override fun showMessage(message: String?) {}
                    })
            }
        }

//        validateLocation {
//            val latitude = driverLocation?.latitude
//            val longitude = driverLocation?.longitude
//            CustomLogger.e("Driver's location >>> $latitude, $longitude")
//
//            val bodyRequest = UpdateDriverLocationDTO(
//                latitude = latitude,
//                longitude = longitude
//            )
//            commonRepo.updateDriverLocation(
//                bodyRequest,
//                object : BaseRepoCallback<ResponseBody> {
//                    override fun apiResponse(data: ResponseBody) {}
//
//                    override fun showMessage(message: String?) {}
//                })
//        }
    }

    private fun validateLocation(onDone: () -> Unit) {
        var logMessage: String
        if (driverLocation == null) {
            val latestJobStatus = DriverRepo.get().latestJobStatus.value

            logMessage = "latestJobStatus: $latestJobStatus - driverLocation == null" +
                    "\n" + "-> Consider to use lastKnownLocationOfDriver"

            if (latestJobStatus == ApiConst.JobStatus.DRIVER_PICKUP_ARRIVED
                || latestJobStatus == ApiConst.JobStatus.DRIVER_DELIVERY_ARRIVED
            ) {
                logMessage = ">>> Use lastKnownLocationOfDriver"
                driverLocation = lastKnownLocationOfDriver
            }

            CustomLogger.e(logMessage)
        }

        onDone()
    }

    private fun requestLocationUpdate() {
//        locationManager.cancel()
//        getLocation()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            val location = result.lastLocation
            CustomLogger.e("Location update callback -->> $location")
            driverLocation = location
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            super.onLocationAvailability(availability)
            val isLocationAvailable = availability.isLocationAvailable
            if (!isLocationAvailable) {
                val logMessage = "onLocationFailed: isLocationAvailable = $availability"
                CustomLogger.e(logMessage)
            }
        }
    }
}