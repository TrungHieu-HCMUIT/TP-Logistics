package com.trunnghieu.tplogisticsapplication.ui.screens.job

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.cso.CsoPhoneNumber
import com.trunnghieu.tplogisticsapplication.databinding.ActivityJobBinding
import com.trunnghieu.tplogisticsapplication.databinding.DialogCsoBinding
import com.trunnghieu.tplogisticsapplication.extensions.navigateTo
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseConnectivityActivity
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppFunctionDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.FragmentNavigator
import com.trunnghieu.tplogisticsapplication.ui.screens.account_settings.AccountSettingsFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.cso.CsoPhoneAdapter
import com.trunnghieu.tplogisticsapplication.ui.screens.job.adapter.MenuAdapter
import com.trunnghieu.tplogisticsapplication.ui.screens.job_history.HistoryFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.login.LoginActivity
import com.trunnghieu.tplogisticsapplication.ui.screens.vehicle_pairing.VehiclePairingFragment
import com.trunnghieu.tplogisticsapplication.ui.widgets.MovableFloatingActionButton
import com.trunnghieu.tplogisticsapplication.utils.constants.Const
import com.trunnghieu.tplogisticsapplication.utils.helper.DeviceHelper
import com.trunnghieu.tplogisticsapplication.utils.helper.SystemHelper

class JobActivity : BaseConnectivityActivity<ActivityJobBinding, JobVM>(), JobUV,
    DrawerLayout.DrawerListener {

    // Static
    companion object {
        private val FRAGMENTS_NOT_SHOW_CALL_CSO = listOf(
            // TODO: Add more fragment
            AccountSettingsFragment::class.java.simpleName,
        )
    }

    override fun layoutRes() = R.layout.activity_job

    override fun createFragmentNavigator(): FragmentNavigator {
        return FragmentNavigator(supportFragmentManager, R.id.view_fragment_container)
    }

    override fun viewModelClass(): Class<JobVM> {
        return JobVM::class.java
    }

    override fun initViewModel(viewModel: JobVM) {
        viewModel.run {
            init(this@JobActivity)
            initLifeCycle(this@JobActivity)
            binding.vm = this
        }
    }

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    // Adapter
    private lateinit var menuAdapter: MenuAdapter

    // CSO
    private var dialogCsoBinding: DialogCsoBinding? = null
    private var csoViewIsShowing = false

    override fun initView() {
        drawer = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawer, R.string.menu_open, R.string.menu_close)
    }

    override fun initData() {
        viewModel.appVersion.value = DeviceHelper.getAppVersion(context)

        // Drawer Layout
        drawer.apply {
            setScrimColor(ContextCompat.getColor(context, R.color.drawerScrim))
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            addDrawerListener(this@JobActivity)
        }

        // Menu
        menuAdapter = MenuAdapter(
            object : BaseItemClickListener<HamburgerMenu> {
                override fun onItemClick(item: HamburgerMenu) {
                    viewModel.handleMenuItemClick(item)
                }
            },
            object : BaseItemClickListener<HamburgerMenu.SubMenu> {
                override fun onItemClick(item: HamburgerMenu.SubMenu) {

                }
            }
        )
        binding.rvMenu.adapter = menuAdapter

        viewModel.run {
            prepareHamburgerMenu(context)
        }

    }

    override fun initAction() {
        getNavigator().apply {
            rootFragment = VehiclePairingFragment.newInstance(
                //TODO: Fix here
                false,
                ""
            )
        }

        binding.btnCallCso.setCustomClickListener(object :
            MovableFloatingActionButton.CustomClickListener {
            override fun onClick(view: View?) {
                var functionDialog: AlertDialog? = null

                dialogCsoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_cso,
                    null, true
                )
                dialogCsoBinding!!.setCancelOnClick {
                    functionDialog?.dismiss()
                    csoViewIsShowing = false
                    showCallCSO(true)
                }
                functionDialog = AppFunctionDialog.get().showCustomLayout(
                    context,
                    dialogCsoBinding!!,
                    Const.DIALOG_CALL_CSO_PERCENT_WIDTH
                )

                // Get CSO phone number
                viewModel.getCSOPhoneNumber()

                csoViewIsShowing = true
                showCallCSO(false)
            }
        })
    }

    override fun accountConflicted() {

    }

    override fun showLocationError() {
    }

    override fun showNetworkError() {
    }

    override fun dismissConnectivityErrorPopup() {
    }

    override fun connectivityChange(isConnected: Boolean) {
    }

    override fun showMenu(show: Boolean) {
        if (show) {
            drawer.openDrawer(GravityCompat.START, true)
        } else {
            drawer.closeDrawers()
        }
    }

    override fun loadHamburgerMenu(menuList: List<HamburgerMenu>) {
        menuAdapter.submitList(menuList)
    }

    override fun goToJobHistory() {
        getNavigator().goTo(HistoryFragment())
    }

    override fun goToAccountSettings() {
        getNavigator().goTo(AccountSettingsFragment())
    }

    override fun goToLogin() {
        navigateTo(LoginActivity::class.java, true)
    }

    override fun gotCsoPhoneNumber(csoPhoneNumbers: List<CsoPhoneNumber>) {
        val adapter = CsoPhoneAdapter(object : BaseItemClickListener<CsoPhoneNumber> {
            override fun onItemClick(item: CsoPhoneNumber) {
                SystemHelper.actionCall(context, item.phoneNo)
            }
        })
        dialogCsoBinding?.rvCsoPhone?.adapter = adapter
        adapter.submitList(csoPhoneNumbers)
    }

    override fun backToVehiclePairing() {
        // TODO: Fix value here
        getNavigator().rootFragment = VehiclePairingFragment.newInstance(
            false,
            ""
        )
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    private fun showCallCSO(show: Boolean) {
        binding.btnCallCso.visibility = if (show) {
            if (getNavigator().activeFragment?.tag in FRAGMENTS_NOT_SHOW_CALL_CSO) return
            if (csoViewIsShowing) return
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}