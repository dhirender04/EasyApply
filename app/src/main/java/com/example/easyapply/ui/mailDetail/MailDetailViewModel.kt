package com.example.easyapply.ui.mailDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.easyapply.common.room.EmailTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
 import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MailDetailViewModel @Inject constructor(private val repository: MailDetailRepository) : ViewModel(){


    val allEmailTemplateList:LiveData<List<EmailTemplate>> = repository.allEmailTemplate
    fun insert(message: EmailTemplate) = viewModelScope.launch {
        repository.insert(message)
    }

    fun update(message: EmailTemplate) = viewModelScope.launch {
        repository.update(message)
    }

    fun delete(message: EmailTemplate) = viewModelScope.launch {
        repository.delete(message)
    }

}