package com.example.easyapply.ui.home

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.easyapply.common.utils.PreferenceUtil
import com.example.easyapply.databinding.FragmentHomeBinding
import com.example.easyapply.utils.Constants
import com.example.easyapply.utils.Utils.byteArrayToFile
import com.example.easyapply.utils.Utils.convertByteArrayToUri
import com.example.easyapply.utils.Utils.fromBase64
import com.example.easyapply.utils.Utils.getFileNameFromUri
import com.example.easyapply.utils.Utils.openFilePicker
import com.example.easyapply.utils.Utils.toBase64
import com.example.easyapply.utils.Utils.uriToByteArray
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var pickPdfLauncher: ActivityResultLauncher<Intent>
    private var uriToByteArray:ByteArray?= null

    @Inject
    lateinit var preferenceUtil: PreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the ActivityResultLauncher
        setUserDefaultData()
        pickPdfLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val uri = result.data?.data
                    uri?.let {
                        uriToByteArray = uriToByteArray(it,requireContext())
                        Log.e(TAG, "onViewCreated:FileValue -- " + it)
                        val getName = getFileNameFromUri(it,requireContext())
                        binding.cvEditText.setText(getName)
                        Log.e(TAG, "onViewCreated:FileValueFullName -- " + getName)
                    }
                }
            }
        binding.apply {
            llCvContainer.setOnClickListener {
                openFilePicker(pickPdfLauncher)
            }
            btnSave.setOnClickListener{
                if (isValidate()){
                    val name = nameEditText.text.toString().trim()
                    val email = emailEditText.text.toString().trim()
                   val cv =  cvEditText.text.toString()
                    preferenceUtil.saveToSharedPreference(requireContext(),Constants.USER_NAME,name)
                    preferenceUtil.saveToSharedPreference(requireContext(),Constants.USER_EMAIL,email)
                    preferenceUtil.saveToSharedPreference(requireContext(),Constants.USER_CV_NAME,cv)
                    val uri = convertByteArrayToUri(uriToByteArray!!,requireContext(),cv)
                    preferenceUtil.saveToSharedPreference(requireContext(),Constants.USER_CV,uri.toString())
//                    preferenceUtil.saveToSharedPreference(requireContext(),Constants.USER_CV,uriToByteArray?.toBase64()?:"")

                }
            }
        }
    }
    private fun setUserDefaultData(){
        binding.apply {
            nameEditText.setText(preferenceUtil.getFromSharePreference(Constants.USER_NAME,"NA"))
            emailEditText.setText(preferenceUtil.getFromSharePreference(Constants.USER_EMAIL,"NA"))
            cvEditText.setText(preferenceUtil.getFromSharePreference(Constants.USER_CV_NAME,"NA"))
           val cvData =  preferenceUtil.getFromSharePreference(Constants.USER_CV,"")?.fromBase64()
            if (cvData?.isNotEmpty()==true){
                Log.e(TAG, "setUserDefaultDataSize:-- "+ cvData.size)
                Log.e(TAG, "setUserDefaultData:-- "+ cvData)
            }

        }
    }
    private fun isValidate():Boolean{
        binding.apply {
            if (nameEditText.text?.isEmpty() == true){
                Toast.makeText(requireContext(), "Enter Name", Toast.LENGTH_SHORT).show()
                return false
            }
            if (emailEditText.text?.isEmpty() == true){
                Toast.makeText(requireContext(), "Enter Email", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }

}