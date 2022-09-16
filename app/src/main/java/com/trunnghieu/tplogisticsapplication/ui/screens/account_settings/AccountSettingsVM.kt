package com.trunnghieu.tplogisticsapplication.ui.screens.account_settings

import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import java.io.File

class AccountSettingsVM : BaseRepoViewModel<AccountRepo, AccountSettingsUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    private val appPrefs = AppPreferences.get()

    val isEditingProfile = MutableLiveData(false)
    val prePhoneNumber = MutableLiveData("")
    val phoneNumber = MutableLiveData("")
    val driverName = MutableLiveData("")
    val company = MutableLiveData("")

    /**
     * On back pressed
     */
    fun backPress() {
        uiCallback?.onFragmentBackPressed()
    }

    /**
     * Go to Change password screen
     */
    fun changePassword() {
        uiCallback?.goToChangePassword()
    }

    /**
     * Select avatar from gallery
     */
    fun selectAvatar() {
        uiCallback?.pickImageFromGallery()
    }

    /**
     * Edit user profile
     */
    fun editProfile() {
        isEditingProfile.value = true
    }

    /**
     * Get account info
     */
    fun getDriverInfo() {

    }

    /**
     * Save new profile
     */
    fun saveProfile() {
        isEditingProfile.value = false
    }

    /**
     * Start upload driver avatar
     */
    fun uploadDriverAvatar(selectedFile: File) {

    }
}