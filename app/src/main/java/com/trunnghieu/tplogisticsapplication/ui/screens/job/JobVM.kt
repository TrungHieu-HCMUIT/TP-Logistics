package com.trunnghieu.tplogisticsapplication.ui.screens.job

import android.content.Context
import androidx.lifecycle.*
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.local.driver.DriverRepo
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.MenuType
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.SubMenuType
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.cso.CsoPhoneNumber
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JobVM : BaseRepoViewModel<AccountRepo, JobUV>() {

    override fun createRepo(): AccountRepo {
        return AccountRepo()
    }

    private lateinit var lifecycleOwner: LifecycleOwner

    val appVersion = MutableLiveData("1.0.0")

    fun initLifeCycle(owner: LifecycleOwner) {
        lifecycleOwner = owner
        lifecycleOwner.lifecycle.addObserver(this)
    }

    // Preferences
    private val appPrefs = AppPreferences.get()
    val appLanguage = LocaleHelper.getLanguageFromLocale()
    private val driverRepo = DriverRepo.get()

    // Job data
    var isRefreshingJobData = false
    var jobWasCancelled = false
    var jobUnAssigned = false
    val showRefreshButton = MutableLiveData(false)
    val latestJob = MutableLiveData<Job>(LocalJob.get().getLatestJob())
    val highlightDataChanged = MutableLiveData(false)
    var alreadyRunResetHighlightUI = false
    val viewJobAssigned = appPrefs.getBoolean(AppPrefs.LOGIN.VIEW_JOB_ASSIGNED)

    /**
     * Show menu
     */
    fun openMenu() {
        uiCallback?.showMenu(true)
    }

    /**
     * Hide menu
     */
    fun closeMenu() {
        uiCallback?.showMenu(false)
    }

    /**
     * Logout current user
     */
    fun logOut() {
        uiCallback?.goToLogin()
    }

    /**
     * Preparing data for Hamburger Menu
     */
    fun prepareHamburgerMenu(context: Context) {
        val hamburgerMenuList =
            mutableListOf(
                HamburgerMenu(
                    type_id = MenuType.JOB_HISTORY.typeId,
                    title = context.getString(R.string.menu_job_history),
                ),
                HamburgerMenu(
                    type_id = MenuType.ACCOUNT_SETTINGS.typeId,
                    title = context.getString(R.string.menu_account_settings),
                )
            )

        hamburgerMenuList.add(
            HamburgerMenu(
                type_id = MenuType.LANGUAGE.typeId,
                title = context.getString(R.string.menu_language),
                listOf(
                    HamburgerMenu.SubMenu(
                        SubMenuType.ENGLISH.typeId,
                        R.drawable.img_lang_en,
                        context.getString(R.string.menu_lang_en)
                    ),
                    HamburgerMenu.SubMenu(
                        SubMenuType.VIETNAMESE.typeId,
                        R.drawable.img_lang_vn,
                        context.getString(R.string.menu_lang_vn)
                    ),
                )
            )
        )

        uiCallback?.loadHamburgerMenu(hamburgerMenuList)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeJobStatus() {
        driverRepo.latestJobStatus.observe(lifecycleOwner, jobStatusObserver)
    }

    private val jobStatusObserver = Observer<ApiConst.JobStatus> { jobStatus ->
        navigateScreenBasedOnStatus(jobStatus)
    }

    private fun navigateScreenBasedOnStatus(jobStatus: ApiConst.JobStatus? = null) {
//        startUpdateDriverLocation()
        when (jobStatus) {
            ApiConst.JobStatus.OPEN,
            ApiConst.JobStatus.ASSIGNED -> {

            }
            ApiConst.JobStatus.DRIVER_JOB_COMPLETED -> {
                val latestJob = LocalJob.get().getLatestJob()
                if (latestJob == null) {
                    uiCallback?.goToStartWork()
                } else {
                    uiCallback?.goToJobSummary()
                }
            }
        }

    }

    /**
     * Change app language
     */
    fun changeAppLanguage(context: Context, typeId: Int) {
        closeMenu()
        val languageCode = when (typeId) {
            SubMenuType.ENGLISH.typeId -> TPLogisticsConst.AppLanguage.ENGLISH
            SubMenuType.VIETNAMESE.typeId -> TPLogisticsConst.AppLanguage.VIETNAMESE
            else -> TPLogisticsConst.AppLanguage.ENGLISH
        }.code
        LocaleHelper.setLocale(context, languageCode)
        uiCallback?.updateLanguage()
    }

    /**
     * Handle onClick of menu
     */
    fun handleMenuItemClick(item: HamburgerMenu) {
        if (item.type_id != MenuType.LANGUAGE.typeId) {
            closeMenu()
        }
        when (item.type_id) {
            MenuType.JOB_HISTORY.typeId -> {
                uiCallback?.goToJobHistory()
            }
            MenuType.ACCOUNT_SETTINGS.typeId -> {
                uiCallback?.goToAccountSettings()
            }
            MenuType.DASHBOARD.typeId -> {

            }
            MenuType.SCAN_QR.typeId -> {

            }
        }
    }

    /**
     * Change job status
     */
    fun changeJobStatus(newJobStatus: ApiConst.JobStatus) {
        driverRepo.updateLatestJobStatus(newJobStatus.name)
    }

    /**
     * Set root fragment as Vehicle Pairing
     */
    fun backToVehiclePairing() {
        uiCallback?.backToVehiclePairing()
    }

    /**
     * Get CSO Phone number
     */
    fun getCSOPhoneNumber() {
        val data = mutableListOf(
            CsoPhoneNumber(phoneNo = "0983 24 92 75", supportName = "TH"),
            CsoPhoneNumber(phoneNo = "0983 24 92 75", supportName = "HT"),
            CsoPhoneNumber(phoneNo = "0983 24 92 75", supportName = "NN"),
        )
        uiCallback?.gotCsoPhoneNumber(data)
    }

    /**
     * Refresh job data
     */
    fun refreshJobData() {

    }

    /**
     * Highlight data changed by display text color is primary color
     * Reset highlight UI after 30s when user first interaction
     */
    fun showHighlightDataChanged(show: Boolean, disableImmediately: Boolean? = false) {
        isRefreshingJobData = false
        if (show) {
            alreadyRunResetHighlightUI = false
            highlightDataChanged.value = true
        } else {
            if (disableImmediately == true) {
                highlightDataChanged.value = false
                return
            }
            if (!alreadyRunResetHighlightUI) {
                alreadyRunResetHighlightUI = true
                CoroutineScope(Dispatchers.IO).launch {
                    delay(TPLogisticsConst.SHOW_HIGHLIGHT_INTERVAL)
                    highlightDataChanged.postValue(false)
                }
            }
        }
    }

}
