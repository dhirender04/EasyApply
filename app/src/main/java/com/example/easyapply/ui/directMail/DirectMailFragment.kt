package com.example.easyapply.ui.directMail

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.easyapply.R
import com.example.easyapply.databinding.FragmentDirectMailBinding
import com.example.easyapply.utils.Constants.MAIL_TO_DATA
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class DirectMailFragment : Fragment() {

    private lateinit var binding: FragmentDirectMailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isEditMode: Boolean = false
    private lateinit var pickCVLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences =
            requireContext().getSharedPreferences("EmailPreferences", Context.MODE_PRIVATE)
        binding = FragmentDirectMailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mailData = arguments?.getString(MAIL_TO_DATA) ?: ""
        Log.e(TAG, "onViewCreated:mailData " + mailData)

        binding.apply {
            // Restore saved data
            etFrom.setText(sharedPreferences.getString("fromEmail", "kumardhirender04@gmail.com"))
            etTo.setText(sharedPreferences.getString("toEmail", mailData))
            etSubject.setText(
                sharedPreferences.getString(
                    "subject",
                    "Application for Android Developer Position"
                )
            )
            etComposeText.setText(sharedPreferences.getString("body", buildEmailBody()))

            toggleEditMode(isEditMode)

//            etComposeText.addTextChangedListener {
//                // Save the updated email body
//                saveEmailData(
//                    etFrom.text.toString(),
//                    etTo.text.toString(),
//                    etSubject.text.toString(),
//                    it.toString()
//                )
//            }
            btEdit.setOnClickListener {
                // Save the updated email body
                if (isEditMode) {
                    // Save the updated email data when switching from edit mode
                    saveEmailData(
                        etFrom.text.toString(),
                        etTo.text.toString(),
                        etSubject.text.toString(),
                        etComposeText.text.toString()
                    )
                }
                isEditMode = !isEditMode
                toggleEditMode(isEditMode)
            }
            btSend.setOnClickListener {
                val cvFileUriString = sharedPreferences.getString("cvFilePath", null)
                val cvFileUri = cvFileUriString?.let { Uri.parse(it) } // Convert String back to Uri
                sendEmailWithAttachment(mailData, etSubject.text.toString(), etComposeText.text.toString(), cvFileUri)
            }


            selectedCV.setOnClickListener {
                pickCVFile()
            }
        }
        pickCVLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    handleFileUri(uri)
                }
            }
        }
    }

    private fun handleFileUri(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, getFileNameFromUri(uri))
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }

        val fileUri = getFileUri(file) // Convert to content:// URI for sharing
        saveCVFilePath(fileUri) // Save the URI in SharedPreferences
        binding.selectedCV.text = file.name // Display the file name
    }

    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndexOrThrow("_display_name"))
                return displayName
            }
        }
        return "unknown_file.pdf" // Fallback file name
    }





    private fun pickCVFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        pickCVLauncher.launch(Intent.createChooser(intent, "Select CV"))
    }
    private fun saveCVFilePath(uri: Uri) {
        sharedPreferences.edit().apply {
            putString("cvFilePath", uri.path.toString())
            apply()
        }
    }

//
//    private fun sendEmailWithAttachment(mailTo: String, subject: String, body: String, cvFileUri: Uri?) {
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            type = "message/rfc822"
//            putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTo))
//            putExtra(Intent.EXTRA_SUBJECT, subject)
//            putExtra(Intent.EXTRA_TEXT, body)
//            cvFileUri?.let {
//                putExtra(Intent.EXTRA_STREAM, it)
//            }
//        }
//
//        startActivity(Intent.createChooser(intent, "Send email..."))
//    }
private fun sendEmailWithAttachment(mailTo: String, subject: String, body: String, cvFileUri: Uri?) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTo))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        putExtra(Intent.EXTRA_STREAM, getFileFromRaw())

//        cvFileUri?.let {
//            putExtra(Intent.EXTRA_STREAM, it)
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to read the URI
//        }
    }
    startActivity(Intent.createChooser(intent, "Send email..."))
}

    private fun getFileUri(file: File): Uri {
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }

    private fun getFileFromRaw(): Uri? {
        // Access the file from the raw folder
        val rawId =
            R.raw.dhirender_cv // replace 'cv' with the actual name of your file in the raw folder
        val file = File(
            requireContext().cacheDir,
            "dhirender_cv.pdf"
        ) // Ensure the file extension matches the type of your CV
        val inputStream = resources.openRawResource(rawId)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }

    private fun buildEmailBody(): String {
        return """
            Hi Hiring Manager,
            
            I am writing to express my interest in the Android Developer position. With over 3 years of experience in mobile app development and a strong proficiency in modern Android technologies, I am excited about the opportunity to contribute to your team.
            
            Thanks and Regards,
            Dhirender Kumar
            Ludhiana, Punjab
        """.trimIndent()
    }

    private fun saveEmailData(fromEmail: String, toEmail: String, subject: String, body: String) {
        sharedPreferences.edit().apply {
            putString("fromEmail", fromEmail)
            putString("toEmail", toEmail)
            putString("subject", subject)
            putString("body", body)
            apply()
        }
    }

    private fun toggleEditMode(enabled: Boolean) {
        binding.apply {
            if (enabled) {
                btEdit.text = "Save"
                // Set button style to outlined
                btEdit.setBackgroundResource(R.drawable.button_solid)
                btEdit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.solid_button_text_color
                    )
                )

            } else {
                btEdit.text = "Edit"
                // Set button style to solid
                btEdit.setBackgroundResource(R.drawable.button_outlined)
                btEdit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.outlined_button_text_color
                    )
                )


            }
            etFrom.isEnabled = enabled
            etTo.isEnabled = enabled
            etSubject.isEnabled = enabled
            etComposeText.isEnabled = enabled
            btSend.isEnabled = !enabled

        }
    }
    companion object {
        private const val PICK_CV_REQUEST_CODE = 1
    }

}