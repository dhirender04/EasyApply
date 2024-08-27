package com.example.easyapply.common.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easyapply.utils.Constants.EMAIL_TEMPLATE_TABLE

@Entity(tableName = "email_template")
data class EmailTemplate(
    @PrimaryKey(autoGenerate = true) val id :Int=0,
    val to:String,
    val from:String,
    val subject:String,
    val body:String,
    val attachmentPdf:String="",
    val selectedCVPdf:ByteArray?
)
