package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class ExteriorChecks(
    var frontRearRegistration: Int = 0,
    var frontRearReflectors: Int = 0,
    var exteriorMirrorsWiperBlades: Int = 0,
    var headlampAim: Int = 0,
    var frontLightsTurnAndHazard: Int = 0,
    var rearLightsHazardBrakeStop: Int = 0,
    var lampsNumberPlateParkingReversing: Int = 0,
    var additionalLamps: Int = 0,
    var fuelTankFillerCap: Int = 0,
    var frontRearTireCondition: Int = 0,
    var tireThreadDepth: Int = 0,
    var wheelNuts: Int = 0,
    var frontRearShockAbsorbers: Int = 0,
    var bumpers: Int = 0,
    var externalProtection: Int = 0,
    var exhaustEmission: Int = 0,
    var trailerCouplingHitches: Int = 0,
    var fifthWheel: Int = 0,
    var generalCondition: Int = 0,
    var comment: String = "",
)