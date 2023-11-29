package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class UnderVehicleChecks(
    var brakesAndPipes: Boolean? = null,
    var parkingBrakeSystem: Boolean? = null,
    var steeringAssemblyDustBoots: Boolean? = null,
    var wheelsAndTires: Boolean? = null,
    var frontSuspension: Boolean? = null,
    var rearSuspension: Boolean? = null,
    var driveShaft: Boolean? = null,
    var engineTransmissionMountings: Boolean? = null,
    var exhaustSystemMountings: Boolean? = null,
    var fuelAndOilLeaks: Boolean? = null,
    var excessiveCorrosion: Boolean? = null,
    var generalConditionAndStructure: Boolean? = null,
    var comment: String = "",
)