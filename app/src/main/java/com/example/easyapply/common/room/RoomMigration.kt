package com.example.easyapply.common.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object RoomMigration {

    val MIGRATION_1_2 = object :Migration(1,2){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE email_template ADD COLUMN selectedCVPdf BLOB")
        }

    }
}