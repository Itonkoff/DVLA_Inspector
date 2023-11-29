@file:OptIn(ExperimentalMaterial3Api::class)

package com.dvla.pvts.dvlainspectorapp.ui.screens.inspection

import android.text.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dvla.pvts.dvlainspectorapp.ui.viewmodels.InspectionViewModel
import gh.gov.dvla.dvlainspector.data.inspection.InspectionState
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel

private var checks = listOf(
    "Interior checks",
    "Exterior (topside) checks",
    "Under bonnet checks",
    "Under vehicle checks",
    "Accessory checks",
    "Motor cycle checks"
)

private var intChecks = listOf(
    "Seats arrangement/ spacing/ secure mounting",
    "Seat head restraints, seat adjustments",
    "Seat belts front and rear presents",
    "Seat belts secured/ condition/ function/ performance",
    "Headlamps, turn signal, hazard switches",
    "Ignition switch - presents/ functional",
    "Speedometer",
    "Tell tale indicators, dash/ panel illumination",
    "Wipers and washers",
    "Wind screen (cracks, visibility), internal obstructions",
    "Interior exterior mirrors",
    "Foot pedals/ Hand break/ Servo operation",
    "Service break test/ performance",
    "Steering wheel and column",
    "Front door (glazing)/ Window tinting",
    "Door handles/ locks/ window control/ hinges/ latches",
    "Horn",
//    "",
)

private var extChecks = listOf(
    "Front/ Rear registration",
    "Reflectors (front/ rear)",
    "Exterior mirrors, wiper blades",
    "Headlamp and headlamp aim",
    "Front lights, including turn signal indicators and hazard lights",
    "Rear lights, hazard lights, brake or stop lights",
    "Lamps for number plate, parking, reversing",
    "Additional/ auxiliary lamps (non complying)",
    "Fuel tank filler cap",
    "Front/ rear wheels and tires (condition)",
    "Tire thread depth",
    "Wheel nuts/ studs",
    "Front/ rear shock absorbers",
    "Bumpers (front and back protection)",
    "External protection",
    "Exhaust emission",
    "Trailer coupling/ hitches - (large trucks only)",
    "Fifth wheel (truck - tractor only)",
    "General condition of the vehicle",
//    ""
)

private var ubChecks = listOf(
    "Vehicle chassis number",
    "Radiator cap",
    "Braking system/ fluid levels",
    "Horses/ tubes",
    "Battery base/ clamps and fasteners",
    "Wiring/ insulation",
    "Fuel and oil leaks",
    "Fuel system",
    "Exhaust system",
    "Steering and suspension systems",
    "Cleanliness",
    "Vehicle structure",
)

private var uvChecks = listOf(
    "Brake pipes and brake systems",
    "Parking brake system",
    "Steering assembly and dust boots",
    "Wheels and tires",
    "Front suspension including shock absorbers",
    "Rear suspension including shock absorbers",
    "Drive shaft",
    "Engine and transmission mountings",
    "Exhaust system mounting",
    "Fuel and oil leaks (regular drip constant flow)",
    "Excessive corrosion",
    "Structure, general vehicle condition",
)

private var accChecks = listOf(
    "Warning triangle",
    "Jack and wheel nut spanner",
    "Spare tire (presence/ condition)",
    "Fire extinguisher",
)

private var mcChecks = listOf(
    "Wiring",
    "Footrests",
    "Steering head bearings",
    "Handle bars",
    "Stands",
    "Chains and guards",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionPoints(
    authorityViewModel: AuthorityViewModel,
    inspectionViewModel: InspectionViewModel,
    navController: NavController,
    bookingId: String,
) {

    var interiorSelected by remember { mutableStateOf(false) }
    var exteriorSelected by remember { mutableStateOf(false) }
    var underBonnetSelected by remember { mutableStateOf(false) }
    var underVehicleSelected by remember { mutableStateOf(false) }
    var accessorySelected by remember { mutableStateOf(false) }
    var motorCycleSelected by remember { mutableStateOf(false) }

    var showSubmissionDialog by remember { mutableStateOf(false) }

    var odometerReading by remember { mutableStateOf("") }

    val state = inspectionViewModel.inspectionState

    val apiKey by authorityViewModel.apiKey.collectAsState()

    fun turnOffAll() {
        interiorSelected = false
        exteriorSelected = false
        underBonnetSelected = false
        underVehicleSelected = false
        accessorySelected = false
        motorCycleSelected = false
    }

    fun triggerInteriorRecompose() {
        interiorSelected = !interiorSelected
        interiorSelected = !interiorSelected
    }

    fun triggerExteriorRecompose() {
        exteriorSelected = !exteriorSelected
        exteriorSelected = !exteriorSelected
    }

    fun triggerUnderBonnetRecompose() {
        underBonnetSelected = !underBonnetSelected
        underBonnetSelected = !underBonnetSelected
    }

    fun triggerUnderVehicleRecompose() {
        underVehicleSelected = !underVehicleSelected
        underVehicleSelected = !underVehicleSelected
    }

    fun triggerAccessoryRecompose() {
        accessorySelected = !accessorySelected
        accessorySelected = !accessorySelected
    }

    fun triggerMotorCycleRecompose() {
        motorCycleSelected = !motorCycleSelected
        motorCycleSelected = !motorCycleSelected
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(checks) { title ->
                    ChecksChip(
                        title = title,
                        selected = when (title) {
                            "Interior checks" -> interiorSelected
                            "Exterior (topside) checks" -> exteriorSelected
                            "Under bonnet checks" -> underBonnetSelected
                            "Under vehicle checks" -> underVehicleSelected
                            "Accessory checks" -> accessorySelected
                            "Motor cycle checks" -> motorCycleSelected
                            else -> false
                        },
                        onSelectedChange = {
                            turnOffAll()
                            when (title) {
                                "Interior checks" -> interiorSelected = it
                                "Exterior (topside) checks" -> exteriorSelected = it
                                "Under bonnet checks" -> underBonnetSelected = it
                                "Under vehicle checks" -> underVehicleSelected = it
                                "Accessory checks" -> accessorySelected = it
                                "Motor cycle checks" -> motorCycleSelected = it
                                else -> {
                                    false
                                }
                            }
                        }
                    )
                }

                item {
                    ElevatedFilterChip(
                        selected = showSubmissionDialog,
                        onClick = {
                            showSubmissionDialog = true
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .padding(start = 24.dp),
                        label = { Text(text = "Submit", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.CloudUpload,
                                contentDescription = null
                            )
                        },
                    )
                }
            })

        OutlinedTextField(
            value = odometerReading,
            onValueChange = {
                odometerReading = it
                state.odometerReading = it.toDouble()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Speed, contentDescription = "Odometer reding icon")
            },
            label = {
                Text(text = "Odometer reading")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedLeadingIconColor = MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            content = {
                item {
                    if (interiorSelected) {
                        GroupListItem(
                            list = intChecks,
                            comment = state.interiorChecks.comment,
                            onCommentChanged = {
                                state.interiorChecks.comment = it
                                triggerInteriorRecompose()
                            },
                            state = state
                        ) { check, value ->
                            when (check) {
                                "Seats arrangement/ spacing/ secure mounting" -> state.interiorChecks.seatArrangement =
                                    value

                                "Seat head restraints, seat adjustments" -> state.interiorChecks.seatHeadRestraintAdjustment =
                                    value

                                "Seat belts front and rear presents" -> state.interiorChecks.seatBeltFrontRearPresence =
                                    value

                                "Seat belts secured/ condition/ function/ performance" -> state.interiorChecks.seatBeltSecuredConditionFunctionPerformance =
                                    value

                                "Headlamps, turn signal, hazard switches" -> state.interiorChecks.headlampTurnSignalHazardSwitch =
                                    value

                                "Ignition switch - presents/ functional" -> state.interiorChecks.ignitionPresenceFunction =
                                    value

                                "Speedometer" -> state.interiorChecks.speedometer = value
                                "Tell tale indicators, dash/ panel illumination" -> state.interiorChecks.tellTaleDashPanelIllumination =
                                    value

                                "Wipers and washers" -> state.interiorChecks.wipersWashers = value
                                "Wind screen (cracks, visibility), internal obstructions" -> state.interiorChecks.windScreen =
                                    value

                                "Interior exterior mirrors" -> state.interiorChecks.interiorExteriorMirrors =
                                    value

                                "Foot pedals/ Hand break/ Servo operation" -> state.interiorChecks.footPedalsHandBrakeServoOperation =
                                    value

                                "Service break test/ performance" -> state.interiorChecks.serviceBrakeTestPerformance =
                                    value

                                "Steering wheel and column" -> state.interiorChecks.steeringWheelColumn =
                                    value

                                "Front door (glazing)/ Window tinting" -> state.interiorChecks.frontDoorGlazingWindowTinting =
                                    value

                                "Door handles/ locks/ window control/ hinges/ latches" -> state.interiorChecks.handlesLocksWindowControl =
                                    value

                                "Horn" -> state.interiorChecks.horn = value
                            }

                            triggerInteriorRecompose()
                        }
                    }
                }

                item {
                    if (exteriorSelected) {
                        GroupListItem(
                            list = extChecks,
                            state = state,
                            comment = state.exteriorChecks.comment,
                            onCommentChanged = {
                                state.exteriorChecks.comment = it
                                triggerExteriorRecompose()
                            },
                        ) { check, value ->
                            when (check) {
                                "Front/ Rear registration" -> state.exteriorChecks.frontRearRegistration =
                                    value

                                "Reflectors (front/ rear)" -> state.exteriorChecks.frontRearReflectors =
                                    value

                                "Exterior mirrors, wiper blades" -> state.exteriorChecks.exteriorMirrorsWiperBlades =
                                    value

                                "Headlamp and headlamp aim" -> state.exteriorChecks.headlampAim =
                                    value

                                "Front lights, including turn signal indicators and hazard lights" -> state.exteriorChecks.frontLightsTurnAndHazard =
                                    value

                                "Rear lights, hazard lights, brake or stop lights" -> state.exteriorChecks.rearLightsHazardBrakeStop =
                                    value

                                "Lamps for number plate, parking, reversing" -> state.exteriorChecks.lampsNumberPlateParkingReversing =
                                    value

                                "Additional/ auxiliary lamps (non complying)" -> state.exteriorChecks.additionalLamps =
                                    value

                                "Fuel tank filler cap" -> state.exteriorChecks.fuelTankFillerCap =
                                    value

                                "Front/ rear wheels and tires (condition)" -> state.exteriorChecks.frontRearTireCondition =
                                    value

                                "Tire thread depth" -> state.exteriorChecks.tireThreadDepth = value
                                "Wheel nuts/ studs" -> state.exteriorChecks.wheelNuts = value
                                "Front/ rear shock absorbers" -> state.exteriorChecks.frontRearShockAbsorbers =
                                    value

                                "Bumpers (front and back protection)" -> state.exteriorChecks.bumpers =
                                    value

                                "External protection" -> state.exteriorChecks.externalProtection =
                                    value

                                "Exhaust emission" -> state.exteriorChecks.exhaustEmission = value
                                "Trailer coupling/ hitches - (large trucks only)" -> state.exteriorChecks.trailerCouplingHitches =
                                    value

                                "Fifth wheel (truck - tractor only)" -> state.exteriorChecks.fifthWheel =
                                    value

                                "General condition of the vehicle" -> state.exteriorChecks.generalCondition =
                                    value
                            }

                            triggerExteriorRecompose()
                        }
                    }
                }

                item {
                    if (underBonnetSelected) {
                        GroupListItem(
                            list = ubChecks,
                            state = state,
                            comment = state.underBonnetChecks.comment,
                            onCommentChanged = {
                                state.underBonnetChecks.comment = it
                                triggerUnderBonnetRecompose()
                            },
                        ) { check, value ->
                            when (check) {
                                "Vehicle chassis number" -> state.underBonnetChecks.chassisNumber =
                                    value

                                "Radiator cap" -> state.underBonnetChecks.radiatorCap = value
                                "Braking system/ fluid levels" -> state.underBonnetChecks.braking =
                                    value

                                "Horses/ tubes" -> state.underBonnetChecks.horsesTubes = value
                                "Battery base/ clamps and fasteners" -> state.underBonnetChecks.battery =
                                    value

                                "Wiring/ insulation" -> state.underBonnetChecks.wiring = value
                                "Fuel and oil leaks" -> state.underBonnetChecks.fuelOilLeaks = value
                                "Fuel system" -> state.underBonnetChecks.fuelSystem = value
                                "Exhaust system" -> state.underBonnetChecks.exhaustSystem = value
                                "Steering and suspension systems" -> state.underBonnetChecks.steeringSuspensionSystem =
                                    value

                                "Cleanliness" -> state.underBonnetChecks.cleanLines = value
                                "Vehicle structure" -> state.underBonnetChecks.vehicleStructure =
                                    value
                            }
                            triggerUnderBonnetRecompose()
                        }
                    }
                }

                item {
                    if (underVehicleSelected) {
                        GroupListItem(
                            list = uvChecks,
                            state = state,
                            comment = state.underVehicleChecks.comment,
                            onCommentChanged = {
                                state.underVehicleChecks.comment = it
                                triggerUnderVehicleRecompose()
                            },
                        ) { check, value ->
                            when (check) {
                                "Brake pipes and brake systems" -> state.underVehicleChecks.brakesAndPipes =
                                    value

                                "Parking brake system" -> state.underVehicleChecks.parkingBrakeSystem =
                                    value

                                "Steering assembly and dust boots" -> state.underVehicleChecks.steeringAssemblyDustBoots =
                                    value

                                "Wheels and tires" -> state.underVehicleChecks.wheelsAndTires =
                                    value

                                "Front suspension including shock absorbers" -> state.underVehicleChecks.frontSuspension =
                                    value

                                "Rear suspension including shock absorbers" -> state.underVehicleChecks.rearSuspension =
                                    value

                                "Drive shaft" -> state.underVehicleChecks.driveShaft = value
                                "Engine and transmission mountings" -> state.underVehicleChecks.engineTransmissionMountings =
                                    value

                                "Exhaust system mounting" -> state.underVehicleChecks.exhaustSystemMountings =
                                    value

                                "Fuel and oil leaks (regular drip constant flow)" -> state.underVehicleChecks.fuelAndOilLeaks =
                                    value

                                "Excessive corrosion" -> state.underVehicleChecks.excessiveCorrosion =
                                    value

                                "Structure, general vehicle condition" -> state.underVehicleChecks.generalConditionAndStructure =
                                    value
                            }
                            triggerUnderVehicleRecompose()
                        }
                    }
                }

                item {
                    if (accessorySelected) {
                        GroupListItem(
                            list = accChecks,
                            state = state,
                            comment = state.accessoryChecks.comment,
                            onCommentChanged = {
                                state.accessoryChecks.comment = it
                                triggerAccessoryRecompose()
                            },
                        ) { check, value ->
                            when (check) {
                                "Warning triangle" -> state.accessoryChecks.warningTriangle = value
                                "Jack and wheel nut spanner" -> state.accessoryChecks.jackWheelNutSpanner =
                                    value

                                "Spare tire (presence/ condition)" -> state.accessoryChecks.spareTire =
                                    value

                                "Fire extinguisher" -> state.accessoryChecks.fireExtinguisher =
                                    value
                            }
                            triggerAccessoryRecompose()
                        }
                    }
                }

                item {
                    if (motorCycleSelected) {
                        GroupListItem(
                            list = mcChecks,
                            state = state,
                            comment = state.motorCycleChecks.comment,
                            onCommentChanged = {
                                state.motorCycleChecks.comment = it
                                triggerMotorCycleRecompose()
                            },
                        ) { check, value ->
                            when (check) {
                                "Wiring" -> state.motorCycleChecks.wiring = value
                                "Footrests" -> state.motorCycleChecks.footrests = value
                                "Steering head bearings" -> state.motorCycleChecks.steeringHeadBearings =
                                    value

                                "Handle bars" -> state.motorCycleChecks.handleBars = value
                                "Stands" -> state.motorCycleChecks.stands = value
                                "Chains and guards" -> state.motorCycleChecks.chainsAndGuards =
                                    value
                            }
                            triggerMotorCycleRecompose()
                        }
                    }
                }
            }
        )

        if (showSubmissionDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSubmissionDialog = false
                },
                icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
                text = { Text(text = "Are you sure you went through all checks and you would like to submit them?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            inspectionViewModel.submitInspection(
                                bookingId = bookingId,
                                apiKey = apiKey,
                                onSuccess = {
                                    // TODO: Add snackbar as primary short message communicator across app
                                    navController.navigate("login-screen/0")
                                },
                                onUnauthorized = {
                                    // TODO: Reorganise here so that navigation flows nicely
                                    authorityViewModel.isUnauthorised()
                                    navController.navigate("login-screen/0")
                                })
                        }) {
                        Text(text = "Yes")
                    }
                }, dismissButton = {
                    TextButton(onClick = { showSubmissionDialog = false }) {
                        Text(text = "No")
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun GroupListItem(
    list: List<String>,
    state: InspectionState,
    comment: String,
    onCommentChanged: (String) -> Unit,
    onStateChanged: (String, Boolean) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEach {
            CheckChip(
                title = it, selected = when (it) {
                    "Seats arrangement/ spacing/ secure mounting" -> state.interiorChecks.seatArrangement
                    "Seat head restraints, seat adjustments" -> state.interiorChecks.seatHeadRestraintAdjustment
                    "Seat belts front and rear presents" -> state.interiorChecks.seatBeltFrontRearPresence
                    "Seat belts secured/ condition/ function/ performance" -> state.interiorChecks.seatBeltSecuredConditionFunctionPerformance
                    "Headlamps, turn signal, hazard switches" -> state.interiorChecks.headlampTurnSignalHazardSwitch
                    "Ignition switch - presents/ functional" -> state.interiorChecks.ignitionPresenceFunction
                    "Speedometer" -> state.interiorChecks.speedometer
                    "Tell tale indicators, dash/ panel illumination" -> state.interiorChecks.tellTaleDashPanelIllumination
                    "Wipers and washers" -> state.interiorChecks.wipersWashers
                    "Wind screen (cracks, visibility), internal obstructions" -> state.interiorChecks.windScreen
                    "Interior exterior mirrors" -> state.interiorChecks.interiorExteriorMirrors
                    "Foot pedals/ Hand break/ Servo operation" -> state.interiorChecks.footPedalsHandBrakeServoOperation
                    "Service break test/ performance" -> state.interiorChecks.serviceBrakeTestPerformance
                    "Steering wheel and column" -> state.interiorChecks.steeringWheelColumn
                    "Front door (glazing)/ Window tinting" -> state.interiorChecks.frontDoorGlazingWindowTinting
                    "Door handles/ locks/ window control/ hinges/ latches" -> state.interiorChecks.handlesLocksWindowControl
                    "Horn" -> state.interiorChecks.horn
                    "Front/ Rear registration" -> state.exteriorChecks.frontRearRegistration
                    "Reflectors (front/ rear)" -> state.exteriorChecks.frontRearReflectors
                    "Exterior mirrors, wiper blades" -> state.exteriorChecks.exteriorMirrorsWiperBlades
                    "Headlamp and headlamp aim" -> state.exteriorChecks.headlampAim
                    "Front lights, including turn signal indicators and hazard lights" -> state.exteriorChecks.frontLightsTurnAndHazard
                    "Rear lights, hazard lights, brake or stop lights" -> state.exteriorChecks.rearLightsHazardBrakeStop
                    "Lamps for number plate, parking, reversing" -> state.exteriorChecks.lampsNumberPlateParkingReversing
                    "Additional/ auxiliary lamps (non complying)" -> state.exteriorChecks.additionalLamps
                    "Fuel tank filler cap" -> state.exteriorChecks.fuelTankFillerCap
                    "Front/ rear wheels and tires (condition)" -> state.exteriorChecks.frontRearTireCondition
                    "Tire thread depth" -> state.exteriorChecks.tireThreadDepth
                    "Wheel nuts/ studs" -> state.exteriorChecks.wheelNuts
                    "Front/ rear shock absorbers" -> state.exteriorChecks.frontRearShockAbsorbers
                    "Bumpers (front and back protection)" -> state.exteriorChecks.bumpers
                    "External protection" -> state.exteriorChecks.externalProtection
                    "Exhaust emission" -> state.exteriorChecks.exhaustEmission
                    "Trailer coupling/ hitches - (large trucks only)" -> state.exteriorChecks.trailerCouplingHitches
                    "Fifth wheel (truck - tractor only)" -> state.exteriorChecks.fifthWheel
                    "General condition of the vehicle" -> state.exteriorChecks.generalCondition
                    "Vehicle chassis number" -> state.underBonnetChecks.chassisNumber
                    "Radiator cap" -> state.underBonnetChecks.radiatorCap
                    "Braking system/ fluid levels" -> state.underBonnetChecks.braking
                    "Horses/ tubes" -> state.underBonnetChecks.horsesTubes
                    "Battery base/ clamps and fasteners" -> state.underBonnetChecks.battery
                    "Wiring/ insulation" -> state.underBonnetChecks.wiring
                    "Fuel and oil leaks" -> state.underBonnetChecks.fuelOilLeaks
                    "Fuel system" -> state.underBonnetChecks.fuelSystem
                    "Exhaust system" -> state.underBonnetChecks.exhaustSystem
                    "Steering and suspension systems" -> state.underBonnetChecks.steeringSuspensionSystem
                    "Cleanliness" -> state.underBonnetChecks.cleanLines
                    "Vehicle structure" -> state.underBonnetChecks.vehicleStructure
                    "Brake pipes and brake systems" -> state.underVehicleChecks.brakesAndPipes
                    "Parking brake system" -> state.underVehicleChecks.parkingBrakeSystem

                    "Steering assembly and dust boots" -> state.underVehicleChecks.steeringAssemblyDustBoots

                    "Wheels and tires" -> state.underVehicleChecks.wheelsAndTires

                    "Front suspension including shock absorbers" -> state.underVehicleChecks.frontSuspension

                    "Rear suspension including shock absorbers" -> state.underVehicleChecks.rearSuspension

                    "Drive shaft" -> state.underVehicleChecks.driveShaft
                    "Engine and transmission mountings" -> state.underVehicleChecks.engineTransmissionMountings

                    "Exhaust system mounting" -> state.underVehicleChecks.exhaustSystemMountings

                    "Fuel and oil leaks (regular drip constant flow)" -> state.underVehicleChecks.fuelAndOilLeaks

                    "Excessive corrosion" -> state.underVehicleChecks.excessiveCorrosion

                    "Structure, general vehicle condition" -> state.underVehicleChecks.generalConditionAndStructure

                    "Warning triangle" -> state.accessoryChecks.warningTriangle
                    "Jack and wheel nut spanner" -> state.accessoryChecks.jackWheelNutSpanner
                    "Spare tire (presence/ condition)" -> state.accessoryChecks.spareTire

                    "Fire extinguisher" -> state.accessoryChecks.fireExtinguisher

                    "Wiring" -> state.motorCycleChecks.wiring
                    "Footrests" -> state.motorCycleChecks.footrests
                    "Steering head bearings" -> state.motorCycleChecks.steeringHeadBearings

                    "Handle bars" -> state.motorCycleChecks.handleBars
                    "Stands" -> state.motorCycleChecks.stands
                    "Chains and guards" -> state.motorCycleChecks.chainsAndGuards

                    else -> false
                }, onStateChange = onStateChanged
            )
        }
    }

    OutlinedTextField(
        value = comment,
        onValueChange = { onCommentChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(96.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Comment, contentDescription = "Odometer reding icon")
        },
        label = {
            Text(text = "Comment")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(focusedLeadingIconColor = MaterialTheme.colorScheme.primary)
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChecksChip(
    title: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
) {
    if (selected) BorderStroke(0.dp, MaterialTheme.colorScheme.surface)
    else BorderStroke(1.dp, Color.Gray)
    FilterChip(
        selected = selected,
        onClick = {
            onSelectedChange(!selected)
        },
        modifier = Modifier
            .height(40.dp),
        label = { Text(text = title, fontSize = 13.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckChip(
    title: String,
    selected: Boolean?,
    onStateChange: (String, Boolean) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var backgroundColor: Color? = null

    if (selected == false) backgroundColor = MaterialTheme.colorScheme.error.copy(alpha = .25f)

    Box {
        if (!TextUtils.isEmpty(title)) {
            (if (selected != false) FilterChipDefaults.filterChipColors() else backgroundColor?.let {
                FilterChipDefaults.filterChipColors(
                    containerColor = it
                )
            })?.let {
                FilterChip(
                    selected = selected == true,
                    label = { Text(text = title, fontSize = 13.sp) },
                    onClick = {
                        showDialog = !showDialog
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(40.dp),
                    colors = it,
                    shape = RoundedCornerShape(6.dp),
                    border = if (selected == false) null else FilterChipDefaults.filterChipBorder()
                )
            }

            if (showDialog) PassFailDialog(
                title = title,
                onPassFail = onStateChange,
                onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun PassFailDialog(title: String, onPassFail: (String, Boolean) -> Unit, onDismiss: () -> Unit) {
    DropdownMenu(expanded = true, onDismissRequest = { onDismiss() }) {
        DropdownMenuItem(text = {
            Text(text = "PASSED", fontSize = 13.sp, fontStyle = FontStyle.Italic)
        }, onClick = {
            onPassFail(title, true)
            onDismiss()
        })
        DropdownMenuItem(text = {
            Text(text = "FAILED", fontSize = 13.sp, fontStyle = FontStyle.Italic)
        }, onClick = {
            onPassFail(title, false)
            onDismiss()
        })
    }
}
