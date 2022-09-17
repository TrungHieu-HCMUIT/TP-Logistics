package com.trunnghieu.tplogisticsapplication.ui.screens.vehicle_pairing

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.FragmentVehiclePairingBinding
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs.UpcomingJobsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehiclePairingFragment : BaseFragment<FragmentVehiclePairingBinding, VehiclePairingVM>(),
    VehiclePairingUV {

    companion object {
        const val ARG_IS_PAIRED = "ARG_IS_PAIRED"
        const val ARG_VEHICLE_NUMBER = "ARG_VEHICLE_NUMBER"

        fun newInstance(isPaired: Boolean = false, vehicleNumber: String = "") =
            VehiclePairingFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_PAIRED, isPaired)
                    putString(ARG_VEHICLE_NUMBER, vehicleNumber)
                }
            }
    }

    override fun layoutRes() = R.layout.fragment_vehicle_pairing

    override fun viewModelClass(): Class<VehiclePairingVM> {
        return VehiclePairingVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: VehiclePairingVM) {
        viewModel.run {
            init(this@VehiclePairingFragment)
            binding.vm = this
        }
        binding.jobVM = jobVM
    }


    override fun initView() {
    }

    override fun initData() {

    }

    override fun initAction() {
    }

    override fun viewUpcomingJobs() {
        navigator.goTo(UpcomingJobsFragment())
    }

}