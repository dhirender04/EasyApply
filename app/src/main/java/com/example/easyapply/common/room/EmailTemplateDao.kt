package com.example.easyapply.common.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmailTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emailData: EmailTemplate)

    @Update
    suspend fun update(emailData: EmailTemplate)

    @Delete
    suspend fun delete(emailData: EmailTemplate)

    @Query("SELECT * FROM email_template")
    fun getAllEmailTemplate(): LiveData<List<EmailTemplate>>
}