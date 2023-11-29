package com.dvla.pvts.dvlainspectorapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dvla.pvts.dvlainspectorapp.ui.screens.auth.LoginScreen
import com.dvla.pvts.dvlainspectorapp.ui.screens.inspection.InspectionPoints
import com.dvla.pvts.dvlainspectorapp.ui.screens.inspection_prep.LaneBookingsScreen
import gh.gov.dvla.dvlainspector.ui.screens.inspection_prep.LanesScreen
import com.dvla.pvts.dvlainspectorapp.ui.viewmodels.InspectionViewModel
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel

val screens = arrayOf("branch-lanes", "lane-bookings", "inspection-points")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InspectionNavHost(
    navController: NavHostController,
    inspectionViewModel: InspectionViewModel = viewModel(),
    authorityViewModel: AuthorityViewModel = viewModel(),
) {

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = "login-screen/0"
    ) {
        composable(
            "login-screen/{route}",
            arguments = listOf(navArgument("route") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            it.arguments?.getInt("route")
                ?.let { index -> LoginScreen(authorityViewModel, navController, screens[index]) }
        }
        composable("branch-lanes") {
            LanesScreen(
                authorityViewModel = authorityViewModel,
                inspectionViewModel = inspectionViewModel,
                navController = navController
            )
        }
        composable(
            "lane-bookings/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            it.arguments?.getString("id")
                ?.let {
                    LaneBookingsScreen(
                        authorityViewModel = authorityViewModel,
                        inspectionViewModel = inspectionViewModel,
                        navController = navController,
                        laneId = it
                    )
                }
        }
        composable(
            "inspection-points/{bookingId}",
            arguments = listOf(navArgument("bookingId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            it.arguments?.getString("bookingId")
                ?.let {
                    InspectionPoints(
                        authorityViewModel=authorityViewModel,
                        inspectionViewModel = inspectionViewModel,
                        navController=navController,
                        bookingId = it
                    )
                }
        }
    }
}