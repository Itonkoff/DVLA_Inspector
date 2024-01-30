package gh.gov.dvla.dvlainspector.ui.screens.inspection_prep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dvla.pvts.dvlainspectorapp.data.network.models.Lane
import com.dvla.pvts.dvlainspectorapp.ui.viewmodels.InspectionViewModel
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanesScreen(
    authorityViewModel: AuthorityViewModel,
    inspectionViewModel: InspectionViewModel,
    navController: NavHostController,
    onCommunicate: (String, Int, () -> Unit) -> Unit,
) {
    val apiKey by authorityViewModel.apiKey.collectAsState()
    inspectionViewModel.getLanes(
        apiKey = apiKey,
        onUnauthorized = {
            authorityViewModel.isUnauthorised()
            navController.navigate("login-screen/0")
        },
        onCommunicate = onCommunicate
    )

    val lanes by inspectionViewModel.affiliatedLanes.collectAsState()
    val isRefreshingLanes by inspectionViewModel.isRefreshingLanes.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshingLanes,
        onRefresh = {
            inspectionViewModel.getLanes(
                apiKey = apiKey,
                onUnauthorized = {
                    authorityViewModel.isUnauthorised()
                    navController.navigate("login-screen/0")
                },
                onCommunicate = onCommunicate
            )
        })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (lanes.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp),
                content = {
                    items(lanes) {
                        Lane(lane = it, navController = navController)
                    }
                }
            )
        } else {
            Text(
                text = "Nothing to show",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 16.sp
            )
        }

        PullRefreshIndicator(
            isRefreshingLanes,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun Lane(lane: Lane, navController: NavHostController) {
    val colorList = listOf(
        Color(243, 134, 43),
        Color(254, 246, 135),
        Color(141, 201, 139),
//        Color(240, 79, 50)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp)
            .height(250.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = colorList, tileMode = TileMode.Clamp))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 32.dp, top = 32.dp)
            ) {
                Text(
                    text = "Lane ${lane.laneNumber}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                val bookingText =
                    if (lane.pendingBookingCount > 1 || lane.pendingBookingCount == 0) "bookings" else "booking"
                Text(
                    text = "${lane.pendingBookingCount} $bookingText",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.Black
                )
            }

            if (lane.pendingBookingCount > 0) {
                IconButton(
                    onClick = { navController.navigate("lane-bookings/${lane.id}") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Proceed icon",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}