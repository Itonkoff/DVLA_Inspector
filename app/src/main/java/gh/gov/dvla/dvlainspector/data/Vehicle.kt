package com.dvla.pvts.dvlainspectorapp.data

@kotlinx.serialization.Serializable
data class Vehicle(
    val type: Int,
    val registrationNumber: String,
    val make: String,
    val model: String,
    val engineNumber: String,
)