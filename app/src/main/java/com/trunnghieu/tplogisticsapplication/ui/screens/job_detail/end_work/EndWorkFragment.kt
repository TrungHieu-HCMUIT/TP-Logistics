package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.end_work

import androidx.core.animation.doOnEnd
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.FragmentEndWorkBinding
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.vehicle_pairing.VehiclePairingFragment

class EndWorkFragment : BaseFragment<FragmentEndWorkBinding, EndWorkVM>(), EndWorkUV {

    override fun layoutRes() = R.layout.fragment_end_work

    override fun viewModelClass(): Class<EndWorkVM> {
        return EndWorkVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: EndWorkVM) {
        viewModel.init(this)
        binding.apply {
            vm = viewModel
            jobVM = this@EndWorkFragment.jobVM
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {
        binding.animationView.addAnimatorUpdateListener { animator ->
            animator.doOnEnd {
                navigator.rootFragment = VehiclePairingFragment.newInstance(
                    false,
                    ""
                )
            }
        }
    }
}
