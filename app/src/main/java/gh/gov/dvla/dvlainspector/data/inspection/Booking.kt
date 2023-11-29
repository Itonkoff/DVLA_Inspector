package gh.gov.dvla.dvlainspector.data.inspection

import com.dvla.pvts.dvlainspectorapp.data.Vehicle

@kotlinx.serialization.Serializable
data class Booking(
    val id: String,
    val proposedBookingDateTime: String,
    var status: Int,
    val vehicle: Vehicle,
)