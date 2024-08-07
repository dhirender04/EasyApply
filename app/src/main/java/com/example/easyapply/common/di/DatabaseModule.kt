package com.example.easyapply.common.di

import android.content.Context
import androidx.room.Room
import com.example.easyapply.common.room.EmailTemplateDao
import com.example.easyapply.common.room.EmailTemplateDatabase
import com.example.easyapply.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext apppContext: Context): EmailTemplateDatabase {
        return Room.databaseBuilder(
            apppContext,
            EmailTemplateDatabase::class.java,
            Constants.EMAIL_TEMPLATE_TABLE
        ).build()
    }
    @Provides
    @Singleton
    fun provideEmailDao(database: EmailTemplateDatabase): EmailTemplateDao {
        return database.emailTemplateDao()
    }
}