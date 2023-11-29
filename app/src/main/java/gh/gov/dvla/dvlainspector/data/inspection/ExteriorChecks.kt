package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class ExteriorChecks(
    var frontRearRegistration: Boolean? = null,
    var frontRearReflectors: Boolean? = null,
    var exteriorMirrorsWiperBlades: Boolean? = null,
    var headlampAim: Boolean? = null,
    var frontLightsTurnAndHazard: Boolean? = null,
    var rearLightsHazardBrakeStop: Boolean? = null,
    var lampsNumberPlateParkingReversing: Boolean? = null,
    var additionalLamps: Boolean? = null,
    var fuelTankFillerCap: Boolean? = null,
    var frontRearTireCondition: Boolean? = null,
    var tireThreadDepth: Boolean? = null,
    var wheelNuts: Boolean? = null,
    var frontRearShockAbsorbers: Boolean? = null,
    var bumpers: Boolean? = null,
    var externalProtection: Boolean? = null,
    var exhaustEmission: Boolean? = null,
    var trailerCouplingHitches: Boolean? = null,
    var fifthWheel: Boolean? = null,
    var generalCondition: Boolean? = null,
    var comment: String = "",
)