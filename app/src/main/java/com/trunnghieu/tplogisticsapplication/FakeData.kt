package com.trunnghieu.tplogisticsapplication

import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job

object FakeData {
    val upcomingJobList = mutableListOf(
        Job(
            bookingNo = "",
            loadNo = 1,
            showDetail = true,
            tripBase = false,
            jobStatus = ApiConst.JobStatus.DRIVER_JOB_STARTED.statusCode,
            customerName = "Hoang Thinh",
            product = "Fragile",
            pickUpLocation = "Tan Binh",
            pickUpLatitude = 10.7898189,
            pickUpLongitude = 106.6414713,
            deliveryLocation = "Linh Trung",
            dischargeLatitude = 10.8700142,
            dischargeLongitude = 106.8008654,
            radius = 5.0,
            pickUpDoneTime = "",
            dischargeTime = "",
        ),
    )
}
