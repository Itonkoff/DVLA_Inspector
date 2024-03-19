package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class InteriorChecks(
    var seatArrangement: Int = 0,
    var seatHeadRestraintAdjustment: Int = 0,
    var seatBeltFrontRearPresence: Int = 0,
    var seatBeltSecuredConditionFunctionPerformance: Int = 0,
    var headlampTurnSignalHazardSwitch: Int = 0,
    var ignitionPresenceFunction: Int = 0,
    var speedometer: Int = 0,
    var tellTaleDashPanelIllumination: Int = 0,
    var wipersWashers: Int = 0,
    var windScreen: Int = 0,
    var interiorExteriorMirrors: Int = 0,
    var footPedalsHandBrakeServoOperation: Int = 0,
    var serviceBrakeTestPerformance: Int = 0,
    var steeringWheelColumn: Int = 0,
    var frontDoorGlazingWindowTinting: Int = 0,
    var handlesLocksWindowControl: Int = 0,
    var horn: Int = 0,
    var comment: String = "",
)