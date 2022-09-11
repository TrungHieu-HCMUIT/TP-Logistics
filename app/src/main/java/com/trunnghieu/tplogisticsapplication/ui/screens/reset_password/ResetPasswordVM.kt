package com.trunnghieu.tplogisticsapplication.ui.screens.reset_password

import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import okhttp3.ResponseBody

class ResetPasswordVM : BaseRepoViewModel<AccountRepo, ResetPasswordUV>() {


    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    val isDriver = MutableLiveData(true)
    val userId = MutableLiveData("")
    val errorMessage = MutableLiveData("")
    val alreadyRequestResetPassword = MutableLiveData(false)

    /**
     * On back pressed
     */
    fun backPress() {
        if (alreadyRequestResetPassword.value == true) {
            alreadyRequestResetPassword.value = false
            return
        }
        uiCallback?.onBackPressed()
    }
}