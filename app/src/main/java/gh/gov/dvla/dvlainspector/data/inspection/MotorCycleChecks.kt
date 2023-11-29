package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class MotorCycleChecks(
    var wiring: Boolean? = null,
    var footrests: Boolean? = null,
    var steeringHeadBearings: Boolean? = null,
    var handleBars: Boolean? = null,
    var stands: Boolean? = null,
    var chainsAndGuards: Boolean? = null,
    var comment: String = "",
)