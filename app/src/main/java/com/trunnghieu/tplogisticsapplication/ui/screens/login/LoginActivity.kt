package com.trunnghieu.tplogisticsapplication.ui.screens.login

import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ActivityLoginBinding
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginVM>(), LoginUV{

    override fun layoutRes() = R.layout.activity_login

    override fun viewModelClass(): Class<LoginVM> {
        return LoginVM::class.java
    }

    override fun initViewModel(viewModel: LoginVM) {
        viewModel.run {
            init(this@LoginActivity)
            initLifeCycle(this@LoginActivity)
            binding.vm = this
        }
    }

    override fun initView() {
        
    }

    override fun initData() {
        
    }

    override fun initAction() {
        
    }

    override fun goToResetPassword() {

    }

    override fun showLanguagePopup() {
        
    }

}