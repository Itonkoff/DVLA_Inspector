package gh.gov.dvla.dvlainspector.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dvla.pvts.dvlainspectorapp.ui.viewmodels.InspectionViewModel
import gh.gov.dvla.dvlainspector.ui.screens.auth.LoginScreen
import gh.gov.dvla.dvlainspector.ui.screens.inspection.InspectionPoints
import gh.gov.dvla.dvlainspector.ui.screens.inspection_prep.LaneBookingsScreen
import gh.gov.dvla.dvlainspector.ui.screens.inspection_prep.LanesScreen
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel
import kotlinx.coroutines.launch

val screens = arrayOf("branch-lanes", "lane-bookings", "inspection-points")
val actions = arrayOf("", "OK", "TRY AGAIN")

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InspectionNavHost(
    navController: NavHostController,
    inspectionViewModel: InspectionViewModel = viewModel(),
    authorityViewModel: AuthorityViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }

    val communicate: (String, Int, () -> Unit) -> Unit = { message, actionLabel, action ->
        scope.launch {
            val result =
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actions[actionLabel],
                    duration = SnackbarDuration.Short
                )


            when (result) {
                SnackbarResult.ActionPerformed -> {
                    if (actionLabel == 2) action()
                    snackbarHostState.currentSnackbarData?.dismiss()
                }

                else -> {}
            }

        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        val padding = it
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
                    ?.let { index ->
                        LoginScreen(
                            authorityViewModel = authorityViewModel,
                            navController = navController,
                            next = screens[index],
                            onCommunicate = { message, actionLabel, action ->
                                communicate(message, actionLabel, action)
                            }
                        )
                    }
            }
            composable("branch-lanes") {
                LanesScreen(
                    authorityViewModel = authorityViewModel,
                    inspectionViewModel = inspectionViewModel,
                    navController = navController,
                    onCommunicate = { message, actionLabel, action ->
                        communicate(message, actionLabel, action)
                    }
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
                            laneId = it,
                            onCommunicate = { message, actionLabel, action ->
                                communicate(message, actionLabel, action)
                            }
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
                            authorityViewModel = authorityViewModel,
                            inspectionViewModel = inspectionViewModel,
                            navController = navController,
                            bookingId = it,
                            onCommunicate = { message, actionLabel, action ->
                                communicate(message, actionLabel, action)
                            }
                        )
                    }
            }
        }
    }
}