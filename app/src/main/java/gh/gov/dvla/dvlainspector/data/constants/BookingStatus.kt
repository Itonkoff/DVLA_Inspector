package com.dvla.pvts.dvlainspectorapp.data.constants

//import kotlinx.serialization.UseSerializers
//
//import com.dvla.pvts.dvlainspectorapp.helpers.EnumAsValueSerializer
//
//class MessageTypeSerializer : EnumAsValueSerializer<BookingStatus>(BookingStatus::opcode)

enum class BookingStatus(val key: Int) {
    Pending(0),
    Inspected(1)
}