package com.trunnghieu.tplogisticsapplication.ui.screens.job

import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface JobUV : BaseUserView {
    fun showMenu(show: Boolean)
    fun loadHamburgerMenu(menuList: List<HamburgerMenu>)
    fun goToAccountSettings()
    fun goToLogin()
    fun backToVehiclePairing()
}