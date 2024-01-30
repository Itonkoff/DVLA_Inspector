package gh.gov.dvla.dvlainspector

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "Utils"

fun createImageFile(): File {
    // Create an image file name
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(),
        "Dvla"
    )
    file.mkdirs()

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        file /* directory */
    )
}