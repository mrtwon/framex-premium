package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Subscription {
    @PrimaryKey @ColumnInfo(name = "id") var id: Int? = null
    @ColumnInfo(name = "content_id") var content_id: Int = 0
    @ColumnInfo(name = "poster") var poster: String? = null
    @ColumnInfo(name = "count") var count: Int = 0

    override fun toString(): String {
        return "count = $count | content_id = $content_id"
    }
}