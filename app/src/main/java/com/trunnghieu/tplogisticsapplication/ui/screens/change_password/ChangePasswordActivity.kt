package com.trunnghieu.tplogisticsapplication.ui.screens.change_password

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.text.method.PasswordTransformationMethod
import androidx.core.content.ContextCompat
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ActivityChangePasswordBinding
import com.trunnghieu.tplogisticsapplication.extensions.navigateTo
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.screens.login.LoginActivity

class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding, ChangePasswordVM>(),
    ChangePasswordUV {

    companion object {
        const val ARG_COMING_FROM_ACCOUNT_SETTINGS = "ARG_COMING_FROM_ACCOUNT_SETTINGS"

        fun newIntent(context: Context, comingFromAccountSettings: Boolean) =
            Intent(context, ChangePasswordActivity::class.java).apply {
                putExtra(ARG_COMING_FROM_ACCOUNT_SETTINGS, comingFromAccountSettings)
            }
    }

    override fun layoutRes() = R.layout.activity_change_password

    override fun viewModelClass(): Class<ChangePasswordVM> {
        return ChangePasswordVM::class.java
    }

    override fun initViewModel(viewModel: ChangePasswordVM) {
        viewModel.run {
            init(this@ChangePasswordActivity)
            binding.vm = this
        }
    }

    private lateinit var normalStrokeColor: ColorStateList
    private lateinit var errorStrokeColor: ColorStateList

    override fun initView() {
        binding.apply {
            inputCurrentPassword.edittext.transformationMethod = PasswordTransformationMethod()
            inputNewPassword.edittext.transformationMethod = PasswordTransformationMethod()
            inputConfirmPassword.edittext.transformationMethod = PasswordTransformationMethod()
        }
    }

    override fun initData() {
        // Get extras
        viewModel.getExtras(intent)

        // Color state list
        normalStrokeColor = ContextCompat.getColorStateList(context, R.color.til_stroke_color)!!
        errorStrokeColor =
            ContextCompat.getColorStateList(context, R.color.til_error_stroke_color)!!
    }

    override fun initAction() {
        viewModel.run {
            currPassword.observe(this@ChangePasswordActivity) {
                if (it.isNotEmpty()) {
                    binding.inputCurrentPassword.textInputLayout.setBoxStrokeColorStateList(
                        normalStrokeColor
                    )
                }
            }

            newPassword.observe(this@ChangePasswordActivity) {
                if (it.isNotEmpty()) {
                    binding.inputNewPassword.textInputLayout.setBoxStrokeColorStateList(
                        normalStrokeColor
                    )
                }
            }

            confirmPassword.observe(this@ChangePasswordActivity) {
                if (it.isNotEmpty()) {
                    binding.inputConfirmPassword.textInputLayout.setBoxStrokeColorStateList(
                        normalStrokeColor
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isComingFromAccountSettings) {
            super.onBackPressed()
            return
        }
        navigateTo(LoginActivity::class.java, true)
    }

    override fun resetValidate() {
        binding.apply {
            inputCurrentPassword.textInputLayout.setBoxStrokeColorStateList(normalStrokeColor)
            inputNewPassword.textInputLayout.setBoxStrokeColorStateList(normalStrokeColor)
            inputConfirmPassword.textInputLayout.setBoxStrokeColorStateList(normalStrokeColor)
        }
    }

    override fun currentPasswordInCorrect() {
        viewModel.errorMessage.value =
            getString(R.string.change_password_err_current_password_incorrect)
        binding.apply {
            inputCurrentPassword.textInputLayout.setBoxStrokeColorStateList(errorStrokeColor)
            inputCurrentPassword.edittext.requestFocus()
        }
    }

    override fun newPasswordNotMatch() {
        viewModel.errorMessage.value =
            getString(R.string.chang_password_err_new_password_not_match)
        binding.apply {
            inputConfirmPassword.textInputLayout.setBoxStrokeColorStateList(errorStrokeColor)
            inputConfirmPassword.edittext.requestFocus()
        }
    }

    override fun newPasswordMustDifferentFromCurrentPassword() {
        viewModel.errorMessage.value =
            getString(R.string.change_password_err_new_password_must_different_current_password)
        binding.apply {
            inputNewPassword.textInputLayout.setBoxStrokeColorStateList(errorStrokeColor)
            inputNewPassword.edittext.requestFocus()
        }
    }

    override fun changePasswordSuccess(message: String) {
        showAlert(
            context.getString(R.string.change_password_msg_success),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    // Finish all current activities
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        )
    }
}