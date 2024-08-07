package com.example.easyapply.ui.mailDetail

import androidx.lifecycle.LiveData
import com.example.easyapply.common.room.EmailTemplate
import com.example.easyapply.common.room.EmailTemplateDao
import javax.inject.Inject
class MailDetailRepository @Inject constructor (private val emailTemplateDao: EmailTemplateDao) {
    val allEmailTemplate:LiveData<List<EmailTemplate>> = emailTemplateDao.getAllEmailTemplate()
    suspend fun insert(emailTemplate: EmailTemplate){
        emailTemplateDao.insert(emailTemplate)
    }
    suspend fun update(emailTemplate: EmailTemplate){
        emailTemplateDao.update(emailTemplate)
    }
    suspend fun delete(emailTemplate: EmailTemplate){
        emailTemplateDao.delete(emailTemplate)
    }
}