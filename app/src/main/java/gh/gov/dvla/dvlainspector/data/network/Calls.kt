package gh.gov.dvla.dvlainspector.data.network

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import gh.gov.dvla.dvlainspector.data.network.models.Credentials
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import java.io.ByteArrayOutputStream
import java.util.UUID

private const val TAG = "Calls"

private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var string: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
//    val index = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val index = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    cursor?.moveToFirst()
    if (index != null) {
        string = cursor.getString(index)
    }
    cursor?.close()
    return string
}

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
    inspectionState: Map<String, Any?>,
    images: MutableList<Bitmap?>,
    contentResolver: ContentResolver,
): HttpResponse =
    ktorHttpClient.submitFormWithBinaryData(
        formData {
            inspectionState.entries.forEach { category ->
                if (category.key == "odometerReading")
                    this.append(FormPart(key = category.key, value = category.value.toString()))
                else {
                    if (category.value is LinkedHashMap<*, *>) {
                        (category.value as LinkedHashMap<*, *>).forEach { check ->
                            this.append(
                                FormPart(
                                    key = "${category.key}.${check.key}",
                                    value = if (check.value != null) check.value else ""
                                )
                            )
                        }
                    }
                }
            }

            for (image in images) {
                val string = UUID.randomUUID().toString()
                val index = images.indexOf(image)
                if (image != null) {
                    this.appendInput(
                        key = "images.[$index]",
                        headers = Headers.build {
                            append(
                                name = HttpHeaders.ContentDisposition,
                                value = "filename=$string.png"
                            )
                        },
                    ) { buildPacket { writeFully(convertBitmapToByteArray(image)) } }
                }
            }
        }
    ) {
        url {
            protocol = URLProtocol.HTTP  // TODO: Change to Https in production
            host = HOST
            port = PORT
            appendPathSegments("api", "inspection")
            parameters.append("xm", id)
        }
        header("x-api-key", apiKey)
        contentType(ContentType.Application.Json)
    }

fun convertBitmapToByteArray(image: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}
