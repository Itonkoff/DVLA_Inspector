package gh.gov.dvla.dvlainspector.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gh.gov.dvla.dvlainspector.data.network.postLogin
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
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

    fun login(email: String, password: String, onCommunicate: (String, Int, () -> Unit) -> Unit) {
        viewModelScope.launch {
            try {
                _error.value = ""
                val response = postLogin(email, password)

                if (response.status == HttpStatusCode.InternalServerError) {
                    onCommunicate("Something went wrong. Please contact support", 1) {}
                }

                if (response.status == HttpStatusCode.BadRequest) {
                    val message: String = response.body()
                    onCommunicate(message, 0) {}
                    _isLogged.value = false
                    Log.i(TAG, "login: BAD REQUEST $message")
                }

                if (response.status.isSuccess()) {
                    val k: String = response.body()
                    _apiKey.value = k.replace("\"", "")
                    _isLogged.value = true
                    onCommunicate("Login succeeded", 1) {}
                    Log.i(TAG, "login: key: ${_apiKey.value}")
                }
            } catch (e: ConnectTimeoutException) {
                Log.e(TAG, "login: EXCEPTION", e)
                onCommunicate("Time out", 1) {}
            } catch (e: Exception) {
                Log.e(TAG, "login: EXCEPTION", e)
            } finally {

            }
        }
    }

    fun isUnauthorised() {
        _apiKey.value = ""
        _isLogged.value = false
    }
}