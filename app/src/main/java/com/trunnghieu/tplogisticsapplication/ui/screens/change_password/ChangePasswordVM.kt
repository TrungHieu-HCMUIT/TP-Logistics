package com.trunnghieu.tplogisticsapplication.ui.screens.change_password

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel

class ChangePasswordVM : BaseRepoViewModel<AccountRepo, ChangePasswordUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    var isComingFromAccountSettings = true
    val errorMessage = MutableLiveData("")
    val currPassword = MutableLiveData("")
    val newPassword = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    /**
     * Get intent extras from activity
     */
    fun getExtras(intent: Intent?) {
        intent?.let {
            isComingFromAccountSettings =
                it.getBooleanExtra(ChangePasswordActivity.ARG_COMING_FROM_ACCOUNT_SETTINGS, false)
        }
    }

    /**
     * On back pressed
     */
    fun backPress() {
        uiCallback?.onBackPressed()
    }

    /**
     * Request to change password
     */
    fun changePassword() {
        // TODO: Handle logic
        uiCallback?.changePasswordSuccess()
    }
}