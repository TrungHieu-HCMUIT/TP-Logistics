package com.trunnghieu.tplogisticsapplication.ui.screens.account_settings

import android.app.Activity
import android.content.Intent
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.FragmentAccountSettingsBinding
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppLoadingDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.change_password.ChangePasswordActivity
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobActivity
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import java.io.File

class AccountSettingsFragment : BaseFragment<FragmentAccountSettingsBinding, AccountSettingsVM>(),
    AccountSettingsUV {

    override fun layoutRes() = R.layout.fragment_account_settings

    override fun viewModelClass(): Class<AccountSettingsVM> {
        return AccountSettingsVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: AccountSettingsVM) {
        viewModel.run {
            init(this@AccountSettingsFragment)
            binding.vm = this
        }
        binding.jobVM = jobVM
    }

    // Upload Progress
    private val progressDialog = AppLoadingDialog.get()

    // Image Picker
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val fileUri = it.data?.data!!
                val selectedFile = File(fileUri.path!!)
                viewModel.uploadDriverAvatar(selectedFile)
            }
        }

    override fun initView() {

    }

    override fun initData() {
        viewModel.getDriverInfo()
    }

    override fun initAction() {

    }

    override fun onFragmentBackPressed() {
        if (viewModel.isEditingProfile.value == true) {
            viewModel.isEditingProfile.value = false
            return
        }
        super.onFragmentBackPressed()
    }

    override fun goToChangePassword() {
        startActivity(ChangePasswordActivity.newIntent(fragmentContext, true))
    }

    override fun goToVerifyPhone(newPhoneNumber: String, expiresTimeInMinutes: Int) {
//        navigator.goTo(PhoneVerifyFragment.newInstance(newPhoneNumber, expiresTimeInMinutes))
    }

    override fun pickImageFromGallery() {
        ImagePicker.with(activity as JobActivity)
            .crop()
            .compress(TPLogisticsConst.FILE.IMAGE_MAX_SIZE)
            .galleryMimeTypes(TPLogisticsConst.FILE.IMAGE_MIME_TYPES)
            .createIntent { pickerIntent ->
                imagePickerLauncher.launch(pickerIntent)
            }
    }

    override fun showProgress(show: Boolean) {
        if (show) progressDialog.showProgress()
        else progressDialog.dismiss()
    }

    override fun progressUpdate(progress: Int) {
        progressDialog.updateProgress(progress)
    }

    override fun uploadAvatarSuccess(avatarUrl: String) {

    }

    override fun uploadAvatarFail() {
        showMessage(fragmentContext.getString(R.string.account_settings_msg_failed_to_upload_profile))
    }
}