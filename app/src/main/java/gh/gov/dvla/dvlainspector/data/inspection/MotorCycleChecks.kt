package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class MotorCycleChecks(
    var wiring: Int = 0,
    var footrests: Int = 0,
    var steeringHeadBearings: Int = 0,
    var handleBars: Int = 0,
    var stands: Int = 0,
    var chainsAndGuards: Int = 0,
    var comment: String = "",
)