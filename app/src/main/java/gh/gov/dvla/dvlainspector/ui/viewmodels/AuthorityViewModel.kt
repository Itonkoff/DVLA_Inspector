package gh.gov.dvla.dvlainspector.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gh.gov.dvla.dvlainspector.data.network.postLogin
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AuthorityViewModel"

class AuthorityViewModel : ViewModel() {
    private val _apiKey = MutableStateFlow("")
    val apiKey = _apiKey.asStateFlow()

    private val _isLogged = MutableStateFlow(false)
    val isLogged = _isLogged.asStateFlow()

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = ""
            val response = postLogin(email, password)
            if (response.status == HttpStatusCode.BadRequest) {
                _error.value = response.body()
                _isLogged.value = false
                Log.i(TAG, "login: error ${error.value}")
            }

            if (response.status.isSuccess()) {
                val k: String = response.body()
                _apiKey.value = k.replace("\"", "")
                _isLogged.value = true
                Log.i(TAG, "login: key: ${_apiKey.value}")
            }
        }
    }

    fun isUnauthorised() {
        _apiKey.value = ""
        _isLogged.value = false
    }
}