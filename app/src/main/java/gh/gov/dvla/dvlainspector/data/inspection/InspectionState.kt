package gh.gov.dvla.dvlainspector.data.inspection

@kotlinx.serialization.Serializable
class InspectionState(
    var odometerReading: Double = 0.0,
    var interiorChecks: InteriorChecks = InteriorChecks(),
    var exteriorChecks: ExteriorChecks = ExteriorChecks(),
    var underBonnetChecks: UnderBonnetChecks = UnderBonnetChecks(),
    var underVehicleChecks: UnderVehicleChecks = UnderVehicleChecks(),
    var accessoryChecks: AccessoryChecks = AccessoryChecks(),
    var motorCycleChecks: MotorCycleChecks = MotorCycleChecks(),
)




