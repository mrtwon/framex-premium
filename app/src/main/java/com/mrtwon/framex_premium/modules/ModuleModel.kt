package com.mrtwon.framex_premium.modules

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mrtwon.framex_premium.MyApplication
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.Model.Model
import com.mrtwon.framex_premium.retrofit.testPOJO.FramexApi
import com.mrtwon.framex_premium.retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleModel {

   @Singleton
   @Provides
   fun getModel(db: Database, videoCdn: VideoCdnApi, kp: KinopoiskApi, rating: XmlParse, fxApi: FramexApi): Model{
      return Model(db, kp, videoCdn, rating, fxApi)
   }

   @Singleton
   @Provides
   fun getDatabase(): Database {
      return Room.databaseBuilder(
          MyApplication.getInstance.applicationContext,
         Database::class.java, "database")
         .createFromAsset("database")
         /*.addMigrations(object: Migration(1){
            override fun migrate(database: SupportSQLiteDatabase){
            *//*  Migration Action *//*
            *//*database.execSQL("""
               CREATE TABLE subscription (
                   id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   content_id INTEGER,
                   count      INTEGER
               );
               CREATE TABLE notification (
                   id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   content_id INTEGER,
                   season     TEXT,
                   series     TEXT
               );
            """.trimIndent())*//*
            }})*/
         .build()
   }
}