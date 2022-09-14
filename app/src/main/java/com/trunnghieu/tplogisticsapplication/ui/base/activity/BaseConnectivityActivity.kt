package com.trunnghieu.tplogisticsapplication.ui.base.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.filter.BroadCastFilter
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.NetworkHelper

abstract class BaseConnectivityActivity<T : ViewDataBinding, VM : BaseViewModel> :
    BaseFragmentBindingActivity<T, VM>() {

    // Location
    private var locationManager: LocationManager? = null
    private var isGpsProviderEnabled = false

    // Network
    private val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"

    protected abstract fun accountConflicted()
    protected abstract fun showLocationError()
    protected abstract fun showNetworkError()
    protected abstract fun dismissConnectivityErrorPopup()
    protected abstract fun connectivityChange(isConnected: Boolean)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun registerReceiver() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val filter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(ACTION_CONNECTIVITY_CHANGE)
            addAction(BroadCastFilter.ACTION_ACCOUNT_CONFLICT)
        }
        context.registerReceiver(connectivityReceiver, filter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterReceiver() {
        context.unregisterReceiver(connectivityReceiver)
    }

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BroadCastFilter.ACTION_ACCOUNT_CONFLICT -> {
                    // TODO: Fix here
//                    ServiceHelper.stopService(context, DriverLocationService::class.java)
                    showAlert(
                        context.getString(R.string.alert_msg_account_conflict),
                        onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                            override fun onPositiveClick() {
                                accountConflicted()
                            }
                        }
                    )
                }
                LocationManager.PROVIDERS_CHANGED_ACTION -> {
                    checkLocation()
                }
                ACTION_CONNECTIVITY_CHANGE -> {
                    checkConnectivity()
                }
            }
        }
    }

    protected fun checkLocation() {
        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGpsProviderEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsProviderEnabled) {
            connectivityChange(false)
            showLocationError()
        } else {
            dismissConnectivityErrorPopup()
        }
    }

    protected fun checkConnectivity() {
        NetworkHelper.isOnline(
            context,
            listener = object : NetworkHelper.NetworkConnectionListener {
                override fun onNetworkConnectionChanges(isConnected: Boolean) {
                    connectivityChange(isConnected)
                    if (isConnected) {
                        dismissConnectivityErrorPopup()
                        return
                    }
                    showNetworkError()
                }
            })
    }
}