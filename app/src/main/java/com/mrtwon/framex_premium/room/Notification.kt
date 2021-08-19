package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity
class Notification {
    @PrimaryKey @ColumnInfo(name = "id" ) var id: Int? = null
    @ColumnInfo(name = "content_id") var content_id: Int = 0
    @ColumnInfo(name = "season") var season: String = ""
    @ColumnInfo(name = "series") var series: String = ""
    @ColumnInfo(name = "ru_title") var ru_title: String = ""
}