/*
Configures and provides app-wide dependencies
(DB, repositories, network, shared prefs)
so you can inject them wherever needed with @Inject.
 */
package com.iti.graduation.recipeapp.di

import android.content.Context
import com.iti.graduation.recipeapp.data.local.MealDatabase
import com.iti.graduation.recipeapp.data.remote.RetrofitInstance
import com.iti.graduation.recipeapp.data.repository.AuthRepository
import com.iti.graduation.recipeapp.data.repository.MealRepository
import com.iti.graduation.recipeapp.utility.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase {
        return MealDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideMealRepository(
        database: MealDatabase
    ): MealRepository {
        return MealRepository(database.mealDao())
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        database: MealDatabase,
        sharedPrefManager: SharedPrefManager
    ): AuthRepository {
        return AuthRepository(database.userDao(), sharedPrefManager)
    }

    @Singleton
    @Provides
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(): RetrofitInstance {
        return RetrofitInstance
    }
}