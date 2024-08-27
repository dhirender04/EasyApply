package com.example.easyapply.common.di

import android.content.Context
import androidx.room.Room
import com.example.easyapply.common.room.EmailTemplateDao
import com.example.easyapply.common.room.EmailTemplateDatabase
import com.example.easyapply.common.room.RoomMigration
import com.example.easyapply.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton  // Ensure the database is provided as a singleton
    fun provideDatabase(@ApplicationContext appContext: Context): EmailTemplateDatabase {
        return Room.databaseBuilder(
            appContext,
            EmailTemplateDatabase::class.java,
            Constants.EMAIL_TEMPLATE_TABLE
        ).addMigrations(RoomMigration.MIGRATION_1_2).build()
    }

    @Provides
    fun provideEmailDao(database: EmailTemplateDatabase): EmailTemplateDao {
        return database.emailTemplateDao()
    }
}
