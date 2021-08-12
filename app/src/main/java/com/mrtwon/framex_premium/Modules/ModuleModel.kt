package com.mrtwon.framex_premium.Modules

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.startandroid.MyApplication
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.Model.Model
import com.mrtwon.framex_premium.Model.ModelApi
import com.mrtwon.framex_premium.Retrofit.TestPOJO.FramexApi
import com.mrtwon.framex_premium.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleModel {

   @Singleton
   @Provides
   fun getNewModel(db: Database, api: FramexApi): ModelApi{
      return ModelApi(db, api)
   }

   @Singleton
   @Provides
   fun getModel(db: Database, videoCdn: VideoCdnApi, kp: KinopoiskApi, rating: XmlParse): Model{
      return Model(db, kp, videoCdn, rating)
   }

   @Singleton
   @Provides
   fun getDatabase(): Database {
      return Room.databaseBuilder(MyApplication.getInstance.applicationContext,
         Database::class.java, "database")
         .createFromAsset("database")
         .addMigrations(object: Migration(10,11){
            override fun migrate(database: SupportSQLiteDatabase){
            /*  Migration Action */
            /*database.execSQL("""
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
            """.trimIndent())*/
            }})
         .build()
   }
}