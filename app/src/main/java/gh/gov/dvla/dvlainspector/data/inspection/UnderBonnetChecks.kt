package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class UnderBonnetChecks(
    var chassisNumber: Int = 0,
    var radiatorCap: Int = 0,
    var braking: Int = 0,
    var horsesTubes: Int = 0,
    var battery: Int = 0,
    var wiring: Int = 0,
    var fuelOilLeaks: Int = 0,
    var fuelSystem: Int = 0,
    var exhaustSystem: Int = 0,
    var steeringSuspensionSystem: Int = 0,
    var cleanLines: Int = 0,
    var vehicleStructure: Int = 0,
    var comment: String = "",
)