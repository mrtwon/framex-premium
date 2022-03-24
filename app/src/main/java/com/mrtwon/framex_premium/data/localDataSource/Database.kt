package com.mrtwon.framex_premium.data.localDataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.NotificationDao
import com.mrtwon.framex_premium.data.entity.RecentDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao

@Database(entities = [FavoriteDao::class, SubscriptionDao::class, NotificationDao::class, RecentDao::class]
    , version = 2, exportSchema = false)
abstract class Database: RoomDatabase(){
    abstract fun roomDao(): RoomDao
}