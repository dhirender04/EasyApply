package com.example.easyapply.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.easyapply.utils.Constants.EMAIL_TEMPLATE_TABLE

@Database(entities = [EmailTemplate::class], version = 2, exportSchema = false)
abstract class EmailTemplateDatabase : RoomDatabase() {
    abstract fun emailTemplateDao(): EmailTemplateDao


//    companion object {
//        @Volatile
//        private var INSTANCE: EmailTemplateDatabase? = null
//        fun getDatabase(context: Context): EmailTemplateDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    EmailTemplateDatabase::class.java,
//                    EMAIL_TEMPLATE_TABLE
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}