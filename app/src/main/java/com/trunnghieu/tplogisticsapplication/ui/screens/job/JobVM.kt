package com.trunnghieu.tplogisticsapplication.ui.screens.job

import android.content.Context
import androidx.lifecycle.*
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.MenuType
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.SubMenuType
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.AccountRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.LocaleHelper

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

    /**
     * Handle onClick of menu
     */
    fun handleMenuItemClick(item: HamburgerMenu) {
        if (item.type_id != MenuType.LANGUAGE.typeId) {
            closeMenu()
        }
        when (item.type_id) {
            MenuType.JOB_HISTORY.typeId -> {

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

}