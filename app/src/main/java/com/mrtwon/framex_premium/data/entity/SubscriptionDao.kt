package com.mrtwon.framex_premium.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex_premium.domain.entity.Subscription
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Subscription")
class SubscriptionDao {
    @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") var id: Int = 0
    @NotNull @ColumnInfo(name = "contentId") var contentId: Int = 0
    @ColumnInfo(name = "ruTitle") var ruTitle: String? = null
    @NotNull @ColumnInfo(name = "episodeCount") var episodeCount: Int = 0
    @ColumnInfo(name = "posterPreview") var posterPreview: String? = null

    fun toSubscription(): Subscription {
        return Subscription(
            id = id,
            contentId = contentId,
            posterPreview = posterPreview,
            ruTitle = ruTitle,
            episodeCount = episodeCount
        )
    }

    companion object{
        fun fromSubscription(subscription: Subscription): SubscriptionDao {
            val dao = SubscriptionDao()
            dao.id = subscription.id
            dao.contentId = subscription.contentId
            dao.ruTitle = subscription.ruTitle
            dao.posterPreview = subscription.posterPreview
            dao.episodeCount = subscription.episodeCount
            return dao
        }
    }
}