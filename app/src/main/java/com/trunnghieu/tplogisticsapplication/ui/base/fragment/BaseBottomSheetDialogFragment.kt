package com.trunnghieu.tplogisticsapplication.ui.base.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppLoadingDialog
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.ViewState
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.*

abstract class BaseBottomSheetDialogFragment<T : ViewDataBinding, VM : BaseViewModel> :
    BottomSheetDialogFragment() {

    protected lateinit var dialogContext: Context

    // View & Binding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: View
    protected lateinit var binding: T

    @LayoutRes
    protected abstract fun layoutRes(): Int

    // ViewModel
    protected lateinit var viewModel: VM
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initViewModel(viewModel: VM)

    // Data & Actions
    protected abstract fun initBottomSheet(): View
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    // Dialog
    private var needShowLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.Base_BottomSheetDialog)

        // Dialog
        AppAlertDialog.get().init(dialogContext)
        AppLoadingDialog.get().init(dialogContext)

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            bottomSheetDialog = dialogInterface as BottomSheetDialog
//            bottomSheetDialog.apply {
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
            bottomSheet = initBottomSheet()
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                isDraggable = true
                isFitToContents = true
                isHideable = false
                peekHeight = dialogContext.resources.getDimension(com.intuit.sdp.R.dimen._48sdp).toInt()
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(dialogContext),
            layoutRes(),
            container,
            false
        )
        binding.lifecycleOwner = this

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
            when (viewState) {
                ViewState.SHOW_LOADING -> showLoading(true)
                ViewState.HIDE_LOADING -> showLoading(false)
                ViewState.SHOW_ERROR -> viewModel.errMessage?.run {
                    showMessage(this)
                }
                ViewState.CLOSE_SCREEN -> closeBottomSheet()
            }
        }
    }

    open fun showBottomSheet(fragmentManager: FragmentManager) {
        isCancelable = false
        show(fragmentManager, this.tag)
    }

    open fun closeBottomSheet() {
        dismiss()
    }

    /**
     * Show loading dialog
     */
    fun showLoading(show: Boolean) {
        dismissDialog()
        needShowLoading = show
        if (show) {
            CoroutineScope(Dispatchers.IO)
                .launch {
                    delay(500L)
                    withContext(Dispatchers.Main) {
                        if (needShowLoading) {
                            AppLoadingDialog.get().show()
                        }
                    }
                }
        }
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
            positiveText,
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
}
