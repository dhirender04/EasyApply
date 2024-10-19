package com.example.easyapply.utils

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

object Utils {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun sendMail(
        context: Context,
        to: String,
        from: String,
        subject: String,
        message: String,
        attachmentUri: Uri? = null
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"// This ensures the intent will be handled by email clients only
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            attachmentUri?.let {
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Send email using:"))
        }
    }

      fun openFilePicker(pickPdfLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "application/pdf" }
        pickPdfLauncher.launch(Intent.createChooser(intent, "Select PDF"))

    }
      fun uriToByteArray(uri: Uri, requireContext: Context): ByteArray? {
        return requireContext.contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }
    }
      fun getFileNameFromUri(uri: Uri, requireContext: Context): String? {
        // Get the ContentResolver instance from the context
        val contentResolver: ContentResolver = requireContext.contentResolver

        // Initialize the variable to store the file name
        var fileName: String? = null

        // Query the ContentResolver for the file metadata
        val cursor = contentResolver.query(uri, null, null, null, null)

        // Use the cursor to extract the file name
        cursor?.use {
            // Get the column index for the display name
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            // Move to the first row of the cursor (should be the only row)
            if (it.moveToFirst()) {
                // Retrieve the file name from the cursor
                fileName = it.getString(nameIndex)
            }
        }

        // Return the file name
        return fileName
    }

    fun ByteArray.toBase64():String{
        return Base64.encodeToString(this,Base64.DEFAULT)
    }
    fun String.fromBase64():ByteArray{
        return Base64.decode(this,Base64.DEFAULT)
    }



    fun byteArrayToFile(byteArray: ByteArray, context: Context, fileName: String): File? {
        return try {
            // Create a file in the cache directory
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { fos ->
                fos.write(byteArray)
                file // Return the created file
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    fun getUriFromFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Ensure you have defined the provider in your AndroidManifest.xml
            file
        )
    }
    fun convertByteArrayToUri(byteArray: ByteArray, context: Context, fileName: String): Uri? {
        // Write ByteArray to a file
        val file = byteArrayToFile(byteArray, context, fileName)

        return file?.let {
            // Get Uri from the created file
            getUriFromFile(context, it)
        }
    }
}
