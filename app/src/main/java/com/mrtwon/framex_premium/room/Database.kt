package com.mrtwon.framex_premium.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Favorite::class, Recent::class, Subscription::class, Notification::class), version = 1)
abstract class Database: RoomDatabase(){
    //getting dao for requesting
    abstract fun dao(): Dao
}