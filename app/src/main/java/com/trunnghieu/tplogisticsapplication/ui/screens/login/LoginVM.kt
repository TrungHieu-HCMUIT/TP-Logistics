package com.trunnghieu.tplogisticsapplication.ui.screens.login

import android.Manifest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences


class LoginVM : BaseRepoViewModel<AccountRepo, LoginUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    val appVersion = MutableLiveData("1.0.0")

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    private val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val locationPermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val appPrefs = AppPreferences.get()

    // Account data
    private val savedPhoneNumber = appPrefs.getString(AppPrefs.LOGIN.PHONE_NUMBER)
    private val savedPassword = appPrefs.getString(AppPrefs.LOGIN.PASSWORD)

    var alreadyRestoreLoginData = false
    private var isLoggedIn = false
    val phoneNumber = MutableLiveData("")
    val password = MutableLiveData("")
    val rememberMe = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun restoreLoginData() {
        if (!alreadyRestoreLoginData) {
            alreadyRestoreLoginData = true
            appPrefs.run {
                isLoggedIn = getBoolean(AppPrefs.LOGIN.IS_LOGIN)

                if (getBoolean(AppPrefs.LOGIN.REMEMBER_ME)) {
                    rememberMe.value = true
                    phoneNumber.value = savedPhoneNumber
                    password.value = savedPassword
                } else {
                    rememberMe.value = false
                }
            }

            askPermissions()
        }
    }

    /**
     * Continue login flow from login data
     */
    fun continueLoginFlow() {
        alreadyRestoreLoginData = false
        if (isLoggedIn) {
            // Show app
            requestLogin(savedPhoneNumber, savedPassword)
        }
    }

    private fun askPermissions() {
        uiCallback?.askPermissions(permissions)
    }

    /**
     * Request location permission
     */
    fun requestLocationPermission() {
        uiCallback?.askLocationPermissions(locationPermissions)
    }

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
        requestLogin()
    }

    fun requestLogin(savedUserName: String? = null, savedPassword: String? = null) {
        errorMessage.value = ""
        uiCallback?.resetValidate()

        val phoneNumber = (
                if (savedUserName.isNullOrEmpty())
                    phoneNumber.value
                else
                    savedUserName
                )?.trim()
        val password = if (savedPassword.isNullOrEmpty()) password.value else savedPassword

        if (phoneNumber?.isEmpty() == true
            && password?.isEmpty() == true
        ) {
            uiCallback?.phoneNumberAndPasswordIsEmpty()
            return
        }

        if (phoneNumber?.isEmpty() == true) {
            uiCallback?.phoneNumberIsEmpty()
            return
        }
        if (password?.isEmpty() == true) {
            uiCallback?.passwordIsEmpty()
            return
        }

        uiCallback?.goToMain()
    }

    /**
     * Show language selection popup
     */
    fun showLanguagePopup() {
        uiCallback?.showLanguagePopup()
    }

}