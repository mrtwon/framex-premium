package com.mrtwon.framex_premium.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
class Favorite {
    @PrimaryKey @NotNull @ColumnInfo(name = "id") var id: Int? = null
    @ColumnInfo(name = "id_content") var id_content: Int = 0
    @ColumnInfo(name = "ru_title") var ru_title: String = ""
    @ColumnInfo(name = "content_type") var content_type: String = ""
    @ColumnInfo(name = "poster") var poster: String? = null
}