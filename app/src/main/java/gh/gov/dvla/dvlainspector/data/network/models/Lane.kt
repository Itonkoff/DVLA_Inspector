package com.dvla.pvts.dvlainspectorapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Lane(var id: String, var laneNumber: Int, var pendingBookingCount: Int)