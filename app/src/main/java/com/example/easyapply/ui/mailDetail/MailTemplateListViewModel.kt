package com.example.easyapply.ui.mailDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.easyapply.common.room.EmailTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MailTemplateListViewModel @Inject constructor(private val repository: MailDetailRepository) :ViewModel() {
    val allEmailTemplateList: LiveData<List<EmailTemplate>> = repository.allEmailTemplate

}