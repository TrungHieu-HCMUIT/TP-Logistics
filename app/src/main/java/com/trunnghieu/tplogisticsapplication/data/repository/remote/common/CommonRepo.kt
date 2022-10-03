package com.trunnghieu.tplogisticsapplication.data.repository.remote.common

import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback
import com.trunnghieu.tplogisticsapplication.data.repository.remote.common.driver_location_update.UpdateDriverLocationDTO
import okhttp3.ResponseBody

class CommonRepo : BaseRepo() {

    /**
     * Get all job type
     */
//    fun getAllJobType(callback: BaseRepoCallback<JobTypesResponse>) {
//        commonServices.getAllJobType().enqueue(getApiCallback(callback))
//    }

    /**
     * Update current driver location
     */
    fun updateDriverLocation(
        bodyRequest: UpdateDriverLocationDTO,
        callback: BaseRepoCallback<ResponseBody>
    ) {
//        commonServices.updateDriverLocation(bodyRequest).enqueue(getApiCallback(callback))
    }

    /**
     * Get all country code
     */
//    fun getAllCountryCode(callback: BaseRepoCallback<CountryCodesResponse>) {
//        commonServices.getAllCountryCode().enqueue(getApiCallback(callback))
//    }
}