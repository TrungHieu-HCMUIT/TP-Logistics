package com.trunnghieu.tplogisticsapplication.ui.screens.job

import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.databinding.ActivityJobBinding
import com.trunnghieu.tplogisticsapplication.extensions.navigateTo
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseConnectivityActivity
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.FragmentNavigator
import com.trunnghieu.tplogisticsapplication.ui.screens.account_settings.AccountSettingsFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.adapter.MenuAdapter
import com.trunnghieu.tplogisticsapplication.ui.screens.login.LoginActivity
import com.trunnghieu.tplogisticsapplication.ui.screens.vehicle_pairing.VehiclePairingFragment
import com.trunnghieu.tplogisticsapplication.utils.helper.DeviceHelper

class JobActivity : BaseConnectivityActivity<ActivityJobBinding, JobVM>(), JobUV,
    DrawerLayout.DrawerListener {

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

    override fun goToAccountSettings() {
        getNavigator().goTo(AccountSettingsFragment())
    }

    override fun goToLogin() {
        navigateTo(LoginActivity::class.java, true)
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
}