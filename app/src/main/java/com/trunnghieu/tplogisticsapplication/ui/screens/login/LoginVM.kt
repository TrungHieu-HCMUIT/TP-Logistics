package com.trunnghieu.tplogisticsapplication.ui.screens.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel


class LoginVM : BaseRepoViewModel<AccountRepo, LoginUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    val appVersion = MutableLiveData("1.0.0")

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    // Driver - D/R
    val isDriver = MutableLiveData(true)

    var alreadyRestoreLoginData = false
    private var isLoggedIn = false
    val phoneNumber = MutableLiveData("")
    val password = MutableLiveData("")
    val rememberMe = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    /**
     * Go to Reset Password screen
     */
    fun goToResetPassword() {
        uiCallback?.goToResetPassword()
    }

    /**
     * Request login when touch on Login button
     */
    fun login() {
    }

    /**
     * Show language selection popup
     */
    fun showLanguagePopup() {
        uiCallback?.showLanguagePopup()
    }

}