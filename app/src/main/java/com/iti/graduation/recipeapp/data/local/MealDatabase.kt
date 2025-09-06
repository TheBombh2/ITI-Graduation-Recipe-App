package com.iti.graduation.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iti.graduation.recipeapp.data.local.dao.MealDao
import com.iti.graduation.recipeapp.data.local.dao.UserDao
import com.iti.graduation.recipeapp.data.local.migrations.MIGRATION_1_2
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.data.model.User
import com.iti.graduation.recipeapp.data.model.UserMealMTM


@Database(
    entities = [Meal::class, User::class, UserMealMTM::class],
    version = 2,
    exportSchema = false
)
abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: MealDatabase? = null

        fun getDatabase(context: android.content.Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}