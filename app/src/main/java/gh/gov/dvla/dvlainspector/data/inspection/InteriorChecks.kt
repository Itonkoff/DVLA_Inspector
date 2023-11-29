package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class InteriorChecks(
    var seatArrangement: Boolean? = null,
    var seatHeadRestraintAdjustment: Boolean? = null,
    var seatBeltFrontRearPresence: Boolean? = null,
    var seatBeltSecuredConditionFunctionPerformance: Boolean? = null,
    var headlampTurnSignalHazardSwitch: Boolean? = null,
    var ignitionPresenceFunction: Boolean? = null,
    var speedometer: Boolean? = null,
    var tellTaleDashPanelIllumination: Boolean? = null,
    var wipersWashers: Boolean? = null,
    var windScreen: Boolean?=null,
    var interiorExteriorMirrors: Boolean? = null,
    var footPedalsHandBrakeServoOperation: Boolean? = null,
    var serviceBrakeTestPerformance: Boolean? = null,
    var steeringWheelColumn: Boolean?=null,
    var frontDoorGlazingWindowTinting: Boolean?=null,
    var handlesLocksWindowControl: Boolean? = null,
    var horn: Boolean? = null,
    var comment: String = "",
    )