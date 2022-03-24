package com.mrtwon.framex_premium.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.Favorite
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Favorite")
class FavoriteDao {
    @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") var id: Int = 0
    @NotNull @ColumnInfo(name = "contentId") var contentId: Int = 0
    @ColumnInfo(name = "ruTitle") var ruTitle: String? = null
    @ColumnInfo(name = "contentType") var contentType: String? = null
    @ColumnInfo(name = "posterPreview") var posterPreview: String? = null

    fun toFavorite(): Favorite {
        return Favorite(
            id = id,
            idContent = contentId,
            ruTitle = ruTitle,
            contentType = ContentEnum.getEnum(contentType),
            posterPreview = posterPreview
        )
    }

    companion object{
        fun fromFavorite(favorite: Favorite): FavoriteDao {
            val dao = FavoriteDao()
            dao.id = favorite.id
            dao.contentId = favorite.idContent
            dao.ruTitle = favorite.ruTitle
            dao.posterPreview = favorite.posterPreview
            dao.contentType = favorite.contentType.type
            return dao
        }
    }
}