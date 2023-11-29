package gh.gov.dvla.dvlainspector.data.network

import com.dvla.pvts.dvlainspectorapp.data.network.HOST
import com.dvla.pvts.dvlainspectorapp.data.network.PORT
import com.dvla.pvts.dvlainspectorapp.data.network.ktorHttpClient
import gh.gov.dvla.dvlainspector.data.inspection.InspectionState
import gh.gov.dvla.dvlainspector.data.network.models.Credentials
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

suspend fun postLogin(email: String, password: String): HttpResponse =
    ktorHttpClient.post {
        url {
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            appendPathSegments("x", "login")
        }
        contentType(ContentType.Application.Json)
        setBody(Credentials(email, password))
    }

suspend fun getAffiliatedLanes(apiKey: String): HttpResponse =
    ktorHttpClient.get {
        url {
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            appendPathSegments("meta", "lanes-affiliated")
        }
        headers {
            header("x-api-key", apiKey)
        }
    }

suspend fun getLaneBookings(apiKey: String, laneId: String): HttpResponse =
    ktorHttpClient.get {
        url {
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            appendPathSegments("meta", "lane-bookings")
            parameter("il", laneId)
        }
        header("x-api-key", apiKey)
    }

suspend fun postInspection(
    id: String,
    apiKey: String,
    inspectionState: InspectionState,
): HttpResponse =
    ktorHttpClient.post {
        url {
            protocol = URLProtocol.HTTP  // TODO: Change to Https in production
            host = HOST
            port = PORT
            appendPathSegments("api", "inspection")
            parameters.append("xm", id)
        }
        header("x-api-key", apiKey)
        contentType(ContentType.Application.Json)
        setBody(inspectionState)
    }