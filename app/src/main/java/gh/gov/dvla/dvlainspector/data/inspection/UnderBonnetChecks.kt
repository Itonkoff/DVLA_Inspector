package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class UnderBonnetChecks(
    var chassisNumber: Boolean? = null,
    var radiatorCap: Boolean? = null,
    var braking: Boolean? = null,
    var horsesTubes: Boolean? = null,
    var battery: Boolean? = null,
    var wiring: Boolean? = null,
    var fuelOilLeaks: Boolean? = null,
    var fuelSystem: Boolean? = null,
    var exhaustSystem: Boolean? = null,
    var steeringSuspensionSystem: Boolean? = null,
    var cleanLines: Boolean? = null,
    var vehicleStructure: Boolean? = null,
    var comment: String = "",
)