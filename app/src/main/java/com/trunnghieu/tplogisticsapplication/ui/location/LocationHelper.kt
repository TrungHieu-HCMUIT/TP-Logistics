package com.trunnghieu.tplogisticsapplication.ui.location

import com.google.android.gms.location.LocationRequest
import com.trunnghieu.tplogisticsapplication.utils.constants.Const
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration
import com.yayandroid.locationmanager.configuration.LocationConfiguration

class LocationHelper {
    companion object {
        @Volatile
        private var instance: LocationHelper? = null

        fun get() = instance ?: synchronized(this) {
            val newInstance = instance ?: LocationHelper().also {
                instance = it
            }
            newInstance
        }
    }

    private var locationConfig: LocationConfiguration? = null


    /**
     * Create config for location manager
     */
    fun createConfigUsingGoogleService(): LocationConfiguration {
        LocationManager.enableLog(Const.DEBUG_MODE)
        locationConfig = LocationConfiguration.Builder()
            .keepTracking(true)
            .useGooglePlayServices(
                GooglePlayServicesConfiguration.Builder()
                    .locationRequest(
                        LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                            .setInterval(TPLogisticsConst.LOCATION.REQUEST_GOOGLE_LOCATION_SERVICE_INTERVAL)
                            .setFastestInterval(TPLogisticsConst.LOCATION.REQUEST_GOOGLE_LOCATION_SERVICE_INTERVAL / 2)
                    )
                    .build()
            )
            .useDefaultProviders(
                DefaultProviderConfiguration.Builder()
                    .requiredTimeInterval(TPLogisticsConst.LOCATION.REQUEST_LOCATION_SERVICE_INTERVAL)
                    .build()
            )
            .build()
        return locationConfig!!
    }
}