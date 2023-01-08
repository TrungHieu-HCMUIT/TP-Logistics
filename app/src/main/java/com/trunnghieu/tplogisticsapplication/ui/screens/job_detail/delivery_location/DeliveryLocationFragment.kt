package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.delivery_location

import android.location.Location
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.SphericalUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps.LocationUV
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps.MapsForJobFragment
import com.trunnghieu.tplogisticsapplication.databinding.FragmentDeliveryLocationBinding

class DeliveryLocationFragment :
    MapsForJobFragment<FragmentDeliveryLocationBinding, DeliveryLocationVM>(),
    DeliveryLocationUV, LocationUV {

    override fun layoutRes() = R.layout.fragment_delivery_location

    override fun viewModelClass(): Class<DeliveryLocationVM> {
        return DeliveryLocationVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: DeliveryLocationVM) {
        viewModel.init(this)
        binding.apply {
            vm = viewModel
            locationVM = this@DeliveryLocationFragment.locationVM
            jobVM = this@DeliveryLocationFragment.jobVM
        }
    }

    private lateinit var mapsBottomSheet: BottomSheetBehavior<View>
    private var executedApi = false

    override fun initMapsView() = binding.viewMaps.mapView

    override fun initView() {
        mapsBottomSheet = BottomSheetBehavior.from(binding.viewMaps.root)
        calculatePeekHeightForBottomSheet(binding.viewJobData) { peekHeight ->
            mapsBottomSheet.peekHeight = peekHeight
        }
    }

    override fun initAction() {
        super.initAction()
        binding.viewMaps.run {
            setExpandCollapseMapsOnClick {
                if (mapsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
                    mapExpanded = false
                    mapsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    mapExpanded = true
                    mapsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    override fun onLocationPermissionGranted() {
        jobVM.run {
            // Observer connectivity changes
//            isLocationOrInternetConnected.observe(this@DeliveryLocationFragment) { isConnected ->
//                viewModel.isConnected = isConnected
//
//                if (!isConnected) {
//                    viewModel.showLocalJob()
//                    return@observe
//                }
//
//                if (!executedApi) {
//                    executedApi = true
//                    viewModel.getJobAtDeliveryArrive()
//                }
//            }

            // Observer latest job
            latestJob.observe(this@DeliveryLocationFragment) { latestJob ->
                locationVM.showDataOnMap(latestJob)
            }
        }
    }

    override fun loadMapData() {
        locationVM.showDataOnMap(jobVM.latestJob.value)
    }

    override fun locationChanged(location: Location) {
        super.locationChanged(location)
        // Update user marker
        val lastKnownLatLng = LatLng(location.latitude, location.longitude)
        userMarker?.position = lastKnownLatLng
        val latestJob = jobVM.latestJob.value ?: return
        val deliveryLatLng = LatLng(latestJob.dischargeLatitude, latestJob.dischargeLongitude)
        val markerBearing = SphericalUtil.computeHeading(lastKnownLatLng, deliveryLatLng).toFloat()
        zoomCameraBetweenTwoLocations(markerBearing, lastKnownLatLng, deliveryLatLng)
//
        // Calculate last known location with pickup location
        viewModel.calculateLocationWithRadius(
            location,
            deliveryLatLng.latitude,
            deliveryLatLng.longitude,
            latestJob.radius
        )
    }

    override fun updateLatestJob(latestJob: Job) {
        jobVM.latestJob.value = latestJob
    }

    override fun showDischargeMaterial(jobStatus: ApiConst.JobStatus) {
//        jobVM.changeJobStatus(jobStatus)
    }

    override fun confirmArriveAtDelivery() {
        showAlert(
            fragmentContext.getString(R.string.job_detail_msg_not_arrive_at_delivery),
            fragmentContext.getString(R.string.job_detail_btn_arrived),
            fragmentContext.getString(R.string.job_detail_btn_continue),
            object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    viewModel.submitDeliveryArrive()
                }

                override fun onNegativeClick() {

                }
            }
        )
    }

    override fun deliveryArriveDone(jobStatus: ApiConst.JobStatus) {
        jobVM.run {
            changeJobStatus(jobStatus)

            // [GOT-450] Disable highlight job when change other job screen
            showHighlightDataChanged(show = false, disableImmediately = true)
        }
    }
}
