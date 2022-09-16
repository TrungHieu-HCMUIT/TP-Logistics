package com.trunnghieu.tplogisticsapplication.data.repository.remote.account

import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback

class AccountRepo : BaseRepo() {

    /**
     * Request login
     */
    fun requestLogin() {

    }

    /**
     * Request reset password
     */
    fun requestResetPassword(

    ) {

    }

    /**
     * Request change password
     */
    fun requestChangePassword(

    ) {

    }

    interface PasswordCallback<T> : BaseRepoCallback<T> {
        fun invalidLoginInfo()
    }

    /**
     * Request vehicle pairing
     */
    fun requestVehiclePairing(

    ) {

    }

    /**
     * Unpair truck
     */
    fun unpairTruck() {

    }

    /**
     * Get account info
     */
    fun getAccountInfo() {

    }

    /**
     * Request OTP code for phone verification
     */
    fun requestOtp() {

    }

    /**
     * Confirm OTP code for phone verification
     */
    fun confirmOtp() {

    }

    /**
     * Update account info
     */
    fun updateAccountInfo(

    ) {

    }

    /**
     * Update driver avatar
     */
    fun updateDriverAvatar(

    ) {

    }

    /**
     * Get CSO Phone number
     */
    fun getCsoPhoneNumber(

    ) {

    }

}