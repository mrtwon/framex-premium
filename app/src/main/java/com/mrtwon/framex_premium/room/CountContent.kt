package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class CountContent {
    @ColumnInfo(name = "count") var count: Int = 0
}