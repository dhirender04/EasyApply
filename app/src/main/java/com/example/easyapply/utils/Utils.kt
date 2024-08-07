package com.example.easyapply.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object Utils {
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
}
