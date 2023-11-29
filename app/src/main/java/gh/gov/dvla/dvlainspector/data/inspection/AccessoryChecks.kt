package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class AccessoryChecks(
    var warningTriangle: Boolean? = null,
    var jackWheelNutSpanner: Boolean? = null,
    var spareTire: Boolean? = null,
    var fireExtinguisher: Boolean? = null,
    var comment: String = "",
)