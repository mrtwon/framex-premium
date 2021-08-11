package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Recent {
    @PrimaryKey @ColumnInfo(name = "id") var id: Int? = null
    @ColumnInfo(name = "id_ref") var idRef: Int? = null
    @ColumnInfo(name = "contentType") var contentType: String? = null
    @ColumnInfo(name = "time") var time: Int? = null
}