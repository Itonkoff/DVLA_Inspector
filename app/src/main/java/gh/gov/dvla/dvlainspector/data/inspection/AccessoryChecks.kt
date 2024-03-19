package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class AccessoryChecks(
    var warningTriangle: Int = 0,
    var jackWheelNutSpanner: Int = 0,
    var spareTire: Int = 0,
    var fireExtinguisher: Int = 0,
    var comment: String = "",
)