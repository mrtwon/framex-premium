package com.mrtwon.framex_premium.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(
    Movie::class, Serial::class, CountriesMovie::class,
    GenresMovie::class, Genres::class, Countries::class,
    Favorite::class, Recent::class, Subscription::class, Notification::class
), version = 11)
abstract class Database: RoomDatabase(){
    //getting dao for requesting
    abstract fun dao(): Dao
}