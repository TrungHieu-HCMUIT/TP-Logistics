package com.trunnghieu.tplogisticsapplication.ui.base.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.RetrofitService
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppLoadingDialog
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.ViewState
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.LocaleHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.*

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    LifecycleObserver {

    protected lateinit var context: Context

    // Binding
    protected lateinit var binding: T

    @LayoutRes
    protected abstract fun layoutRes(): Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    // ViewModel
    protected lateinit var viewModel: VM
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initViewModel(viewModel: VM)

    // Dialog
    private var needShowLoading = false
    private var showLoadingJob: Job? = null

    private lateinit var defaultAppLanguage: String
    open fun loadDefaultAppLanguage(language: TPLogisticsConst.AppLanguage) = Unit

    override fun attachBaseContext(newBase: Context) {
        defaultAppLanguage = AppPreferences.get().getString(
            LocaleHelper.SELECTED_LANGUAGE,
            if (
                TPLogisticsConst.AppLanguage.values().any {
                    it.code == LocaleHelper.getLanguageFromLocale()
                }
            ) {
                LocaleHelper.getLanguageFromLocale()
            } else {
                TPLogisticsConst.AppLanguage.ENGLISH.code
            }
        )

        super.attachBaseContext(
            ViewPumpContextWrapper.wrap(
                LocaleHelper.setLocale(
                    newBase,
                    defaultAppLanguage
                )
            )
        )
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        // Add LifeCycle observer
        lifecycle.addObserver(this)

        // DataBinding
        binding = DataBindingUtil.setContentView(this, layoutRes())
        binding.lifecycleOwner = this

        // ViewModel
        viewModel = ViewModelProvider(this)[viewModelClass()]
            .also { _viewModel ->
                initViewModel(_viewModel)
                _viewModel.viewState.observe(this) { viewState ->
                    when (viewState) {
                        ViewState.SHOW_LOADING -> showLoading(true)
                        ViewState.HIDE_LOADING -> showLoading(false)
                        ViewState.SHOW_ERROR -> _viewModel.errMessage?.run {
                            if (this.isNotEmpty()) {
                                showMessage(this)
                            }
                        }
                    }
                }
            }

        loadDefaultAppLanguage(
            TPLogisticsConst.AppLanguage.values().find { it.code == defaultAppLanguage }!!
        )

        // Init
        initView()
        initData()
        initAction()
    }

    override fun onStart() {
        super.onStart()

        // Dialog
        AppAlertDialog.get().init(this)
        AppLoadingDialog.get().init(this)
    }

    /**
     * Hide keyboard when click outside EditText
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            try {
                val currFocus = currentFocus
                if (currFocus is EditText) {
                    val outRect = Rect()
                    currFocus.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        currFocus.clearFocus()
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(currFocus.windowToken, 0)
                    }
                }
            } catch (ignore: Exception) {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Show loading dialog
     */
    fun showLoading(show: Boolean) {
        dismissDialog()
        needShowLoading = show
        if (show) {
            showLoadingJob = CoroutineScope(Dispatchers.IO).launch {
                delay(1000L)
                withContext(Dispatchers.Main) {
                    if (needShowLoading) {
                        AppLoadingDialog.get().show()
                    }
                }
                delay(RetrofitService.REQ_TIME_OUT * 1000)
                withContext(Dispatchers.Main) {
                    viewModel.viewState.value = ViewState.HIDE_LOADING
                }
            }
            showLoadingJob!!.start()
        } else {
            showLoadingJob?.cancel()
            showLoadingJob = null
        }
    }

    /**
     * Show toast
     */
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show error dialog
     */
    fun showError() {
        showMessage(getString(R.string.alert_msg_an_error_has_occurred))
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
        dismissDialog()
        AppAlertDialog.get().show(
            message,
            positiveText ?: context.getString(R.string.alert_btn_positive),
            negativeText,
            onClickListener
        )
    }

    /**
     * Dismiss dialog
     */
    private fun dismissDialog() {
        needShowLoading = false
        AppLoadingDialog.get().dismiss()
        AppAlertDialog.get().dismiss()
    }

    /**
     * Request permission
     */
    fun requestPermission(
        explainPermission: String,
        permissions: List<String>,
        callback: RequestPermissionCallback
    ) {
        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        callback.onPermissionGranted()
                    } else {
                        showExplainPermissionDialog(explainPermission, callback)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionList: MutableList<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }

            })
            .check()
    }

    private fun showExplainPermissionDialog(
        explainPermission: String,
        callback: RequestPermissionCallback
    ) {
        showAlert(
            explainPermission,
            getString(R.string.alert_btn_go_to_settings),
            getString(R.string.alert_btn_negative),
            object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    goToAppSettings()
                }

                override fun onNegativeClick() {
                    callback.onPermissionDenied()
                }
            }
        )
    }

    private fun goToAppSettings() {
        val uri = Uri.fromParts("package", packageName, null)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .apply { data = uri }
        startActivity(intent)
    }

    interface RequestPermissionCallback {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }
}