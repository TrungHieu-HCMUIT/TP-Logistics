package com.trunnghieu.tplogisticsapplication.ui.base.activity

import androidx.databinding.ViewDataBinding
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.FragmentNavigator
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel

abstract class BaseFragmentBindingActivity<T : ViewDataBinding, VM : BaseViewModel> :
    BaseFullscreenActivity<T, VM>() {

    protected abstract fun createFragmentNavigator(): FragmentNavigator

    private var navigator: FragmentNavigator? = null

    fun getNavigator(): FragmentNavigator {
        if (navigator == null) {
            navigator = createFragmentNavigator()
        }
        return navigator!!
    }

    override fun onBackPressed() {
        navigator?.let {
            if (it.size > 0) {
                if (navigator?.activeFragment is BaseFragment<*, *>) {
                    (navigator?.activeFragment as BaseFragment<*, *>).onFragmentBackPressed()
                }
                return
            }
        }
        super.onBackPressed()
    }
}