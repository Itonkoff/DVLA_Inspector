package gh.gov.dvla.dvlainspector.ui.screens.inspection_prep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.dvla.pvts.dvlainspectorapp.data.network.models.LaneBooking
import com.dvla.pvts.dvlainspectorapp.ui.viewmodels.InspectionViewModel
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LaneBookingsScreen(
    authorityViewModel: AuthorityViewModel,
    inspectionViewModel: InspectionViewModel,
    navController: NavHostController,
    laneId: String,
    onCommunicate: (String, Int, () -> Unit) -> Unit,
) {
    val apiKey by authorityViewModel.apiKey.collectAsState()
    inspectionViewModel.getBookings(
        apiKey = apiKey,
        laneId = laneId,
        onUnauthorized = {
            authorityViewModel.isUnauthorised()
            navController.navigate("login-screen/0")
        },
        onCommunicate = onCommunicate
    )

    val bookings by inspectionViewModel.laneBookings.collectAsState()
    val isRefreshingBookings by inspectionViewModel.isRefreshingBookings.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshingBookings,
        onRefresh = {
            inspectionViewModel.getBookings(
                apiKey = apiKey,
                laneId = laneId,
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
        if (bookings.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp),
                content = {
                    items(bookings) {
                        LaneBooking(navController = navController, booking = it)
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
            isRefreshingBookings,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun LaneBooking(booking: LaneBooking, navController: NavHostController) {
    val colorList = listOf(
        Color(243, 134, 43),
        Color(254, 246, 135),
        Color(141, 201, 139),
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp)
            .height(200.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = colorList, tileMode = TileMode.Clamp))
        ) {
            Text(
                text = booking.vin,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
                    .align(Alignment.TopCenter),
                textAlign = TextAlign.Center,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Column(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .align(Alignment.CenterStart)
            ) {
                (if (booking.regNo != null) booking.regNo else "-")?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(
                    text = booking.vehicleType,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 14.dp, top = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.proposedTime,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                IconButton(
                    onClick = { navController.navigate("inspection-points/${booking.id}") }
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
