package com.dvla.pvts.dvlainspectorapp.ui.viewmodels

import android.content.ContentResolver
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvla.pvts.dvlainspectorapp.data.network.models.Lane
import com.dvla.pvts.dvlainspectorapp.data.network.models.LaneBooking
import gh.gov.dvla.dvlainspector.data.inspection.InspectionState
import gh.gov.dvla.dvlainspector.data.network.getAffiliatedLanes
import gh.gov.dvla.dvlainspector.data.network.getLaneBookings
import gh.gov.dvla.dvlainspector.data.network.postInspection
import gh.gov.dvla.dvlainspector.helpers.toMap
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "InspectionViewModel"

class InspectionViewModel : ViewModel() {

    private val _inspectionState = MutableStateFlow(InspectionState())
    val inspectionState: StateFlow<InspectionState> = _inspectionState.asStateFlow()

    private val _affiliatedLanes = MutableStateFlow(listOf<Lane>())
    val affiliatedLanes: StateFlow<List<Lane>> = _affiliatedLanes.asStateFlow()

    private val _laneBookings = MutableStateFlow(listOf<LaneBooking>())
    val laneBookings: StateFlow<List<LaneBooking>> = _laneBookings.asStateFlow()

    private val _isRefreshingLanes = MutableStateFlow(false)
    val isRefreshingLanes: StateFlow<Boolean> = _isRefreshingLanes.asStateFlow()

    private val _isRefreshingBookings = MutableStateFlow(false)
    val isRefreshingBookings: StateFlow<Boolean> = _isRefreshingBookings.asStateFlow()

    private val _isPostingInspection = MutableStateFlow(false)
    val isPostingInspection: StateFlow<Boolean> = _isPostingInspection.asStateFlow()

    fun getLanes(
        apiKey: String,
        onUnauthorized: () -> Unit,
        onCommunicate: (String, Int, () -> Unit) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                _isRefreshingLanes.value = true
                val response = getAffiliatedLanes(apiKey)

                if (response.status == HttpStatusCode.InternalServerError) {
                    onCommunicate("Something went wrong", 2) {
                        getLanes(
                            apiKey = apiKey,
                            onUnauthorized = onUnauthorized,
                            onCommunicate = onCommunicate
                        )
                    }
                    return@launch
                }

                if (response.status == HttpStatusCode.Unauthorized) {
                    onCommunicate("You are not logged in", 1) {}
                    onUnauthorized()
                    return@launch
                }

                if (response.status.isSuccess()) _affiliatedLanes.value = response.body()

            } catch (e: ConnectTimeoutException) {
                Log.e(TAG, "login: EXCEPTION", e)
                onCommunicate("Time out", 1) {}
            } catch (ex: Exception) {
                Log.e(TAG, "getLanes: An Exception Occurred while getting affiliated lanes", ex)
            } finally {
                _isRefreshingLanes.value = false
            }
        }
    }


    fun getBookings(
        apiKey: String,
        laneId: String,
        onUnauthorized: () -> Unit,
        onCommunicate: (String, Int, () -> Unit) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                _isRefreshingBookings.value = true
                val response = getLaneBookings(apiKey, laneId)

                if (response.status == HttpStatusCode.InternalServerError) {
                    onCommunicate("Something went wrong", 2) {
                        getBookings(
                            apiKey = apiKey,
                            laneId = laneId,
                            onUnauthorized = onUnauthorized,
                            onCommunicate = onCommunicate
                        )
                    }
                    return@launch
                }

                if (response.status == HttpStatusCode.Unauthorized) {
                    onCommunicate("You are not logged in", 1) {}
                    onUnauthorized()
                    return@launch
                }

                if (response.status.isSuccess()) _laneBookings.value = response.body()

            } catch (e: ConnectTimeoutException) {
                Log.e(TAG, "login: EXCEPTION", e)
                onCommunicate("Time out", 1) {}
            } catch (ex: Exception) {
                Log.e(TAG, "getLanes: An Exception Occurred while getting affiliated lanes", ex)
            } finally {
                _isRefreshingBookings.value = false
            }
        }
    }

    fun submitInspection(
        bookingId: String,
        apiKey: String,
        images: MutableList<Bitmap?>,
        contentResolver: ContentResolver,
        onSuccess: () -> Unit,
        onUnauthorized: () -> Unit,
        onCommunicate: (String, Int, () -> Unit) -> Unit,
    ) {

        viewModelScope.launch {
            try {
                _isPostingInspection.value = true
                val response = postInspection(
                    id = bookingId,
                    apiKey = apiKey,
                    inspectionState = toMap(_inspectionState.value),
                    images =images,
                    contentResolver=contentResolver
                )

                if (response.status == HttpStatusCode.InternalServerError) {
                    onCommunicate("Something went wrong", 2) {
                        submitInspection(
                            bookingId = bookingId,
                            apiKey = apiKey,
                            images = images,
                            onSuccess = onSuccess,
                            onUnauthorized = onUnauthorized,
                            onCommunicate = onCommunicate,
                            contentResolver = contentResolver
                        )
                    }
                    return@launch
                }

                if (response.status == HttpStatusCode.OK) {
                    _inspectionState.value = InspectionState()
                    onCommunicate("Inspection recorded successfully", 1) {}
                    onSuccess()
                }

                if (response.status == HttpStatusCode.Unauthorized) {
                    onCommunicate("You are not logged in", 1) {}
                    onUnauthorized()
                }

            } catch (e: ConnectTimeoutException) {
                Log.e(TAG, "login: EXCEPTION", e)
                onCommunicate("Time out", 1) {}
            } catch (ex: Exception) {
                Log.e(TAG, "submitInspection: An Exception Occurred while posting inspection", ex)
            } finally {
                _isPostingInspection.value = false
            }
        }
    }

    fun updateState(state: InspectionState) {
        _inspectionState.value = InspectionState().clone(state)
    }

    fun isVehicleAMotorCycle(bookingId: String): Boolean? {
        val find = _laneBookings.value.find { it.id == bookingId }
        if (find != null) {
            return find.vehicleType == "MOTOR BIKE"
        }
        return null
    }
}