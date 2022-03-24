package com.mrtwon.framex_premium.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mrtwon.framex_premium.app.MyApplication
import com.mrtwon.framex_premium.data.localDataSource.Database
import com.mrtwon.framex_premium.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @AppScope
    @Provides
    fun provideDatabase(context: Context): Database{
        return Room.databaseBuilder(context, Database::class.java, "database")
            .addMigrations(object: Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {

                }

            })
            .createFromAsset("database")
            .build()
    }

    @AppScope
    @Provides
    fun provideCacheSharedPreferences(context: Context): SharedPreferences{
        return MyApplication.getInstance.getSharedPreferences("firebase-cache", Context.MODE_PRIVATE)
    }
}