package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Recent {
    @PrimaryKey @ColumnInfo(name = "id") var id: Int? = null
    @ColumnInfo(name = "id_content") var id_content = 0
    @ColumnInfo(name = "content_type") var content_type: String = ""
    @ColumnInfo(name = "poster") var poster: String? = null
    @ColumnInfo(name = "time") var time: Int = 0
}