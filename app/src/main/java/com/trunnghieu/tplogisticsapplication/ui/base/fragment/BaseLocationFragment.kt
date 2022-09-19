package com.trunnghieu.tplogisticsapplication.ui.base.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.ui.location.LocationHelper
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.listener.LocationListener
import kotlinx.coroutines.*

abstract class BaseLocationFragment<T : ViewDataBinding, VM : BaseViewModel> :
    BaseFragment<T, VM>(), BaseActivity.RequestPermissionCallback, LocationListener {

    private val jobVM by activityViewModels<JobVM>()

    private var currentLocation: Location? = null

    open fun onLocationPermissionGranted() = Unit
    open fun initLocationManager(locationManager: LocationManager) = Unit

    override fun initAction() {
        requestPermission(
            fragmentContext.getString(R.string.permission_msg),
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), this
        )
    }

    override fun onPermissionDenied() {
        onFragmentBackPressed()
    }

    override fun onPermissionGranted() {
        initLocation()
    }

    override fun getLocationConfiguration(): LocationConfiguration {
        return LocationHelper.get().createConfigUsingGoogleService()
    }

    override fun locationChanged(location: Location) {
        super.locationChanged(location)
        currentLocation = location
    }

    override fun onLocationFailed(type: Int) {
        super.onLocationFailed(type)
        currentLocation = null
    }

    override fun onProviderDisabled(provider: String) {
        currentLocation = null
    }

    protected fun initLocation() {
        // TODO: Uncomment here
//        jobVM.startUpdateDriverLocation()
        onLocationPermissionGranted()
        getLocation()
        initLocationManager(fragmentContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }

    /**
     * Get current location
     *
     * @return the current location
     */
    @SuppressLint("MissingPermission")
    protected fun getCurrentLocation(gotLocation: (Location?) -> Unit) {
        val isWaitingForLocation = locationManager.isWaitingForLocation
        // TODO: Uncomment here
//        CustomLogger.d("isWaitingForLocation: $isWaitingForLocation")

        if (!isWaitingForLocation) {
            gotLocation(currentLocation)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    getCurrentLocation(gotLocation)
                }
            }
        }
    }

    protected fun updateInputUIForValidWeight(
        validWeight: Boolean,
        textInputLayout: TextInputLayout
    ) {
        val normalStrokeColor =
            ContextCompat.getColorStateList(fragmentContext, R.color.til_stroke_color)!!
        val errorStrokeColor =
            ContextCompat.getColorStateList(fragmentContext, R.color.til_error_stroke_color)!!
        if (validWeight) {
            textInputLayout.setBoxStrokeColorStateList(normalStrokeColor)
            textInputLayout.editText?.setTextColor(
                ContextCompat.getColor(
                    fragmentContext,
                    R.color.textPrimary
                )
            )
        } else {
            textInputLayout.setBoxStrokeColorStateList(errorStrokeColor)
            textInputLayout.editText?.setTextColor(
                ContextCompat.getColor(
                    fragmentContext,
                    R.color.error
                )
            )
        }
    }
}