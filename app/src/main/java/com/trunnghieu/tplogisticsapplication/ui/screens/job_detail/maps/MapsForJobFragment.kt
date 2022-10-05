package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps

import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.view.doOnLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseLocationFragment
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst

abstract class MapsForJobFragment<T : ViewDataBinding, VM : BaseViewModel> :
    BaseLocationFragment<T, VM>(), OnMapReadyCallback, LocationUV {

    protected val locationVM by viewModels<LocationVM>()

    // Maps
    private lateinit var mapView: MapView
    private var gMaps: GoogleMap? = null

    protected var userMarker: Marker? = null

    protected abstract fun initMapsView(): MapView
    protected abstract fun loadMapData()

    override fun initData() {
        // ViewModel
        locationVM.init(this)

        // Setup Google Maps
        mapView = initMapsView()
        mapView.apply {
            getMapAsync(this@MapsForJobFragment)
            onCreate(null)
            onResume()
        }
        MapsInitializer.initialize(fragmentContext)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMaps = googleMap
        gMaps!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json))
    }

    override fun initLocationManager(locationManager: LocationManager) {
        locationVM.initLocation(locationManager)
    }

    override fun locationChanged(location: Location) {
        super.locationChanged(location)
        locationVM.run {
            onLocationChanged(location)
            loadMapData()
        }
    }

    override fun showDataOnMap(
        userLatLng: LatLng,
        pickupName: String,
        pickupLatLng: LatLng,
        dischargeName: String,
        dischargeLatLng: LatLng
    ) {
        // User location
        if (userLatLng.latitude != 0.0 || userLatLng.longitude != 0.0) {
            userMarker = addMarker(latLng = userLatLng, markerRes = R.drawable.marker_user_location)
        }
        gMaps?.apply {
            clear()

            userMarker?.let { _userMarker ->
                addMarker(
                    _userMarker.title,
                    _userMarker.position,
                    R.drawable.marker_user_location
                )?.apply {
                    isFlat = true
                }
            }

            // Pickup location
            if (pickupLatLng.latitude != 0.0 || pickupLatLng.longitude != 0.0) {
                addMarker(pickupName, pickupLatLng, R.drawable.marker_pickup_location)
            }

            // Delivery location
            if (dischargeLatLng.latitude != 0.0 || dischargeLatLng.longitude != 0.0) {
                addMarker(dischargeName, dischargeLatLng, R.drawable.marker_discharge_location)
            }

            // LatLng bound builder
            val latLngBoundsBuilder = LatLngBounds.builder()
                .include(pickupLatLng)
                .include(dischargeLatLng)
            if (userLatLng.latitude != 0.0 && userLatLng.longitude != 0.0) {
                latLngBoundsBuilder.include(userLatLng)
            }

            // Zoom camera
            setOnMapLoadedCallback {
                animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        latLngBoundsBuilder.build(),
                        TPLogisticsConst.LOCATION.CAMERA_PADDING
                    )
                )
            }
        }
    }

    override fun zoomCameraToLastKnownLocation(lastKnownLocation: LatLng) {
        gMaps?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                lastKnownLocation,
                TPLogisticsConst.LOCATION.CAMERA_ZOOM
            )
        )
    }

    override fun openMap(mapUrl: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(mapUrl)
        }.also {
            startActivity(it)
        }
    }

    private fun addMarker(title: String? = null, latLng: LatLng, markerRes: Int): Marker? {
        return gMaps?.addMarker(
            MarkerOptions()
                .title(title)
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(markerRes))
        )
    }

    protected fun zoomCameraBetweenTwoLocations(
        markerBearing: Float,
        firstLatLng: LatLng,
        secondLatLng: LatLng
    ) {
        // LatLng bound builder
        val latLngBoundsBuilder = LatLngBounds.builder()
            .include(firstLatLng)
            .include(secondLatLng)

        // Zoom camera
        gMaps?.run {
            userMarker?.apply {
                setAnchor(0.5f, 0.5f)
                rotation = markerBearing
            }
            setOnMapLoadedCallback {
                animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        latLngBoundsBuilder.build(),
                        TPLogisticsConst.LOCATION.CAMERA_PADDING
                    )
                )
            }
        }
    }

    protected fun calculatePeekHeightForBottomSheet(jobDataView: View, onDone: (Int) -> Unit) {
        jobDataView.doOnLayout {
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val screenDensity = Resources.getSystem().displayMetrics.density
            val jobDataHeight = it.measuredHeight
            val peekHeight = ((screenHeight - jobDataHeight) / screenDensity).toInt()
            onDone(peekHeight)
        }
    }
}