package com.mrtwon.framex_premium.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.Recent
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Recent")
class RecentDao {
    @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") var id: Int = 0
    @NotNull @ColumnInfo(name = "contentId") var contentId: Int = 0
    @NotNull @ColumnInfo(name = "contentType") var contentType: String = ""
    @ColumnInfo(name = "posterPreview") var posterPreview: String? = null
    @NotNull @ColumnInfo(name = "time") var time: Int = 0

    fun toRecent(): Recent {
        return Recent(
            id = id,
            idContent = contentId,
            contentType = ContentEnum.getEnum(contentType),
            posterPreview = posterPreview,
            time = time
        )
    }

    companion object{
        fun fromRecent(recent: Recent): RecentDao {
            val dao = RecentDao()
            dao.id = recent.id
            dao.contentId = recent.idContent
            dao.contentType = recent.contentType.type
            dao.posterPreview = recent.posterPreview
            dao.time = recent.time
            return dao
        }
    }
}