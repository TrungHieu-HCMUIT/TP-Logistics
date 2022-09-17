package com.trunnghieu.tplogisticsapplication.ui.screens.vehicle_pairing

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

class VehiclePairingVM : BaseRepoViewModel<AccountRepo, VehiclePairingUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    private val appPrefs = AppPreferences.get()
    // TODO: Fix here to false
    val isPaired = MutableLiveData(true)
    val vehicleNumber = MutableLiveData("")

    /**
     * Get available job and start work
     */
    fun startWork() {
        // TODO: Fix flow here, check view assigned job is yes or no
        uiCallback?.viewUpcomingJobs()
    }

}