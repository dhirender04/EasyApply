package com.example.easyapply.ui.mailDetail

import android.app.Activity.RESULT_OK
import android.app.Notification.Action
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.PixelCopy.Request
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.easyapply.R
import com.example.easyapply.databinding.FragmentMailDetailBinding
import com.example.easyapply.common.room.EmailTemplate
import com.example.easyapply.utils.Constants.MAIL_TO_DATA
import com.example.easyapply.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailDetailFragment : Fragment() {
    private val viewModel: MailDetailViewModel by viewModels()
    private lateinit var pickPdfLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: FragmentMailDetailBinding
    private var uriToByteArray:ByteArray?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMailDetailBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allEmailTemplateList.observe(viewLifecycleOwner, Observer { mails ->
            Log.e(TAG, "onViewCreated:Mails " + mails)
        })

        // Initialize the ActivityResultLauncher
        pickPdfLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val uri = result.data?.data
                    uri?.let {
                          uriToByteArray = uriToByteArray(it)
                        Log.e(TAG, "onViewCreated:FileValue -- " + it)
                        val getName = getFileNameFromUri(it)
                        binding.selectedCV.text = getName
                        Log.e(TAG, "onViewCreated:FileValueFullName -- " + getName)
                    }
                }
            }

        val isMailTemplateFromEdit = arguments?.getBoolean(getString(R.string.mailtemplatefromedit))
        val mailData = arguments?.getString(MAIL_TO_DATA)
        Log.e(TAG, "onViewCreated:mailData " + mailData)
        Log.e(TAG, "onViewCreated: " + isMailTemplateFromEdit)
        isMailTemplateFromEdit.takeIf { it == true }?.let { templateEdit() }
            ?: templateAdd() // check come from edit or add template

        binding.btSave.setOnClickListener {
        }
        binding.btncvUpload.setOnClickListener {
            openFilePicker()
        }
        binding.apply {
            btSaveRoom.setOnClickListener {
                if (fromVallidation()) {
                    val email = EmailTemplate(
                        to = etFrom.text.toString().trim(),
                        from = etTo.text.toString().trim(),
                        subject = etSubject.text.toString().trim(),
                        body = textInputEditText.text.toString().trim(),
                        attachmentPdf = "",
                        selectedCVPdf = uriToByteArray
                    )
                    Log.e(TAG, "onViewCreated:------>> "+email )
                    viewModel.insert(email)
                }
            }
        }
    }

    private fun fromVallidation(): Boolean {
        binding.apply {
            val email = etFrom.text.toString().trim()
            val etTo = etTo.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Email is required", Toast.LENGTH_SHORT).show()
                return false
            } else if (!email.matches(Utils.emailPattern.toRegex())) {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
                return false
            }
            if (etTo.isEmpty()) {
                Toast.makeText(requireContext(), "Email is required", Toast.LENGTH_SHORT).show()
                return false
            } else if (!etTo.matches(Utils.emailPattern.toRegex())) {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
                return false
            }
            if (etSubject.text.toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Subject must be mention", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (textInputEditText.text.toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Please write your email", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }
        return true
    }

    private fun templateEdit() {
        binding.apply {
            Log.e(TAG, "onViewCreated: Edit Called")
        }

    }

    private fun templateAdd() {
        binding.apply {
            Log.e(TAG, "onViewCreated: Add Called")
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "application/pdf" }
        pickPdfLauncher.launch(Intent.createChooser(intent, "Select PDF"))

    }

    private fun getFileNameFromUri(uri: Uri): String? {
        // Get the ContentResolver instance from the context
        val contentResolver: ContentResolver = requireContext().contentResolver

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

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return requireContext().contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }
    }


}