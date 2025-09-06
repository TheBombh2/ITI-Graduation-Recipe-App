package com.iti.graduation.recipeapp.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS user_meals (
                userId INTEGER NOT NULL,
                mealId TEXT NOT NULL,
                PRIMARY KEY(userId, mealId)
            )
        """.trimIndent())
    }
}