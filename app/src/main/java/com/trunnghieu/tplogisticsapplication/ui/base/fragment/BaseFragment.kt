package com.trunnghieu.tplogisticsapplication.ui.base.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseFragmentBindingActivity
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.ViewState
import com.trunnghieu.tplogisticsapplication.ui.location.LocationHelper
import com.yayandroid.locationmanager.base.LocationBaseFragment
import com.yayandroid.locationmanager.configuration.LocationConfiguration

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : LocationBaseFragment() {

    protected lateinit var fragmentContext: Context

    // Binding
    protected lateinit var binding: T

    @LayoutRes
    protected abstract fun layoutRes(): Int

    // ViewModel
    protected lateinit var viewModel: VM
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initViewModel(viewModel: VM)

    // Data & Actions
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    // Location
    open fun locationChanged(location: Location) = Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(fragmentContext),
            layoutRes(),
            container,
            false
        )
        binding.apply {
            root.setOnTouchListener { v: View, _: MotionEvent ->
                v.isClickable = true
                v.isFocusable = true
                false
            }
            lifecycleOwner = this@BaseFragment
        }

        // ViewModel
        viewModel = ViewModelProvider(this)[viewModelClass()]
            .apply {
                initViewModel(this)
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initAction()

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState?.run {
                when (viewState) {
                    ViewState.SHOW_LOADING -> showLoading(true)
                    ViewState.HIDE_LOADING -> showLoading(false)
                    ViewState.SHOW_ERROR -> viewModel.errMessage?.run {
                        showMessage(this)
                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        locationChanged(location)
    }

    override fun onLocationFailed(type: Int) {

    }

    override fun getLocationConfiguration(): LocationConfiguration {
        return LocationHelper.get().createConfigUsingGoogleService()
    }

    val navigator: FragmentNavigator
        get() = (fragmentContext as BaseFragmentBindingActivity<*, *>?)!!.getNavigator()

    open fun onFragmentBackPressed() {
        navigator.goOneBack()
    }

    protected fun getBaseActivity() = activity as BaseActivity<*, *>?

    /**
     * Show loading dialog
     */
    fun showLoading(show: Boolean) {
        (activity as? BaseActivity<*, *>)?.showLoading(show)
    }

    /**
     * Show toast
     */
    fun showToast(message: String) {
        Toast.makeText(fragmentContext, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show error dialog
     */
    private fun showError() {
        (activity as? BaseActivity<*, *>)?.showError()
    }

    /**
     * Show error dialog
     */
    fun showMessage(message: String?) {
        showAlert(message = message)
    }

    /**
     * Show alert dialog with full options
     * ------------------------------------------
     * | Title                                  |
     * | Message                                |
     * ------NEGATIVE BUTTON---POSITIVE BUTTON---
     */
    fun showAlert(
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onClickListener: AppAlertDialog.AlertDialogOnClickListener? = null
    ) {
        (activity as? BaseActivity<*, *>)?.showAlert(
            message,
            positiveText,
            negativeText,
            onClickListener
        )
    }

    /**
     * Request permission
     */
    fun requestPermission(
        explainPermission: String,
        permissions: List<String>,
        callback: BaseActivity.RequestPermissionCallback
    ) {
        (activity as? BaseActivity<*, *>)?.requestPermission(
            explainPermission,
            permissions,
            callback
        )
    }
}