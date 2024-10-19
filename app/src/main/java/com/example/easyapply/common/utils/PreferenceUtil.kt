package com.example.easyapply.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.easyapply.utils.Constants
import com.google.android.material.color.utilities.Contrast
import javax.inject.Inject

class PreferenceUtil @Inject constructor(private val sharedPreferences: SharedPreferences) {
   fun saveToSharedPreference(context: Context,key:String, value:String){
        sharedPreferences.edit().putString(key,value).apply() // not block main thred Asynchronous - apply()
       Toast.makeText(context, "Data saved successfully!", Toast.LENGTH_SHORT).show() // Show a Toast message

//        sharedPreferences.edit().putString(key,value).commit() //  block main thred Synchronous - commit()

   }

    fun getFromSharePreference(key:String,defaultValue:String):String?{
        return sharedPreferences.getString(key,defaultValue)
    }
}