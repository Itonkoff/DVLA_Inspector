package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class UnderVehicleChecks(
    var brakesAndPipes: Int = 0,
    var parkingBrakeSystem: Int = 0,
    var steeringAssemblyDustBoots: Int = 0,
    var wheelsAndTires: Int = 0,
    var frontSuspension: Int = 0,
    var rearSuspension: Int = 0,
    var driveShaft: Int = 0,
    var engineTransmissionMountings: Int = 0,
    var exhaustSystemMountings: Int = 0,
    var fuelAndOilLeaks: Int = 0,
    var excessiveCorrosion: Int = 0,
    var generalConditionAndStructure: Int = 0,
    var comment: String = "",
)