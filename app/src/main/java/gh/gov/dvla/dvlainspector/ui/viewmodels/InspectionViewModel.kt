package com.dvla.pvts.dvlainspectorapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvla.pvts.dvlainspectorapp.data.network.models.Lane
import com.dvla.pvts.dvlainspectorapp.data.network.models.LaneBooking
import gh.gov.dvla.dvlainspector.data.inspection.InspectionState
import gh.gov.dvla.dvlainspector.data.network.getAffiliatedLanes
import gh.gov.dvla.dvlainspector.data.network.getLaneBookings
import gh.gov.dvla.dvlainspector.data.network.postInspection
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "InspectionViewModel"

class InspectionViewModel : ViewModel() {

    var inspectionState by mutableStateOf(InspectionState())

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

    fun getLanes(apiKey: String, onUnauthorized: () -> Unit) {
        viewModelScope.launch {
            try {
                _isRefreshingLanes.value = true
                val response = getAffiliatedLanes(apiKey)
                if (response.status == HttpStatusCode.Unauthorized) {
                    onUnauthorized()
                    return@launch
                } else if (response.status.isSuccess()) _affiliatedLanes.value = response.body()
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
    ) {
        viewModelScope.launch {
            try {
                _isRefreshingBookings.value = true
                val response = getLaneBookings(apiKey, laneId)
                if (response.status == HttpStatusCode.Unauthorized) {
                    onUnauthorized()
                    return@launch
                } else if (response.status.isSuccess()) _laneBookings.value = response.body()
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
        onSuccess: () -> Unit,
        onUnauthorized: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                _isPostingInspection.value = true
                val response = postInspection(
                    id = bookingId,
                    apiKey = apiKey,
                    inspectionState = inspectionState
                )
                _isPostingInspection.value = false

                if (response.status == HttpStatusCode.OK){
                    inspectionState = InspectionState()
                    onSuccess()
                }

                if (response.status == HttpStatusCode.Unauthorized) onUnauthorized()

            } catch (ex: Exception) {
                Log.e(TAG, "submitInspection: An Exception Occurred while posting inspection", ex)
            }
        }
    }
}