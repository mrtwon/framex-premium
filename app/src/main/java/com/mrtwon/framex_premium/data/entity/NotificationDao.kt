package com.mrtwon.framex_premium.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex_premium.domain.entity.Notification
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Notification")
class NotificationDao {
    @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") var id: Int = 0
    @NotNull @ColumnInfo(name = "contentId") var contentId: Int = 0
    @ColumnInfo(name = "ruTitle") var ruTitle: String? = null
    @NotNull @ColumnInfo(name = "season") var season: String = ""
    @NotNull @ColumnInfo(name = "episode") var episode: String = ""

    fun toNotification(): Notification {
        return Notification(
            id = id,
            idContent = contentId,
            season = season,
            episode = episode,
            ruTitle = ruTitle
        )
    }

    companion object{
        fun fromNotification(notify: Notification): NotificationDao {
            val dao = NotificationDao()
            dao.id = notify.id
            dao.contentId = notify.idContent
            dao.ruTitle = notify.ruTitle
            dao.season = notify.season
            dao.episode = notify.episode
            return dao
        }
    }
}