package com.dvla.pvts.dvlainspectorapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LaneBooking(
    var id: String,
    var vin: String,
    var regNo: String?,
    var vehicleType: String,
    var proposedTime: String,
)