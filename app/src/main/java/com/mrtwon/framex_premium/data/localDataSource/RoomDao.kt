package com.mrtwon.framex_premium.data.localDataSource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.NotificationDao
import com.mrtwon.framex_premium.data.entity.RecentDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao


@Dao
interface RoomDao {
    //LiveData
    @Query("SELECT * FROM Favorite")
    fun getFavoriteLiveData(): LiveData<List<FavoriteDao>>
    @Query("SELECT * FROM Recent")
    fun getRecentLiveData(): LiveData<List<RecentDao>>
    @Query("SELECT * FROM Notification")
    fun getNotificationLiveData(): LiveData<List<NotificationDao>>
    @Query("SELECT * FROM Subscription")
    fun getSubscriptionLiveData(): LiveData<List<SubscriptionDao>>


    // Favorite
    @Insert
    fun addFavorite(favorite: FavoriteDao)
    @Query("DELETE FROM Favorite WHERE contentId = :id AND contentType = :contentType")
    fun removeFavorite(id: Int, contentType: String)
    @Query("SELECT * FROM Favorite WHERE contentId = :contentId AND contentType = :contentType")
    fun existFavoriteLiveData(contentId: Int, contentType: String): LiveData<FavoriteDao?>
    @Query("SELECT * FROM Favorite WHERE contentId = :contentId AND contentType = :contentType")
    fun existFavorite(contentId: Int, contentType: String): FavoriteDao?

    //Subscription
    @Insert
    fun addSubscription(subscription: SubscriptionDao)
    @Query("DELETE FROM Subscription WHERE contentId = :id")
    fun removeSubscription(id: Int)
    @Update
    fun updateSubscription(subscription: SubscriptionDao)
    @Query("SELECT * FROM Subscription WHERE contentId = :contentId")
    fun existSubscriptionLiveData(contentId: Int): LiveData<SubscriptionDao?>
    @Query("SELECT * FROM Subscription WHERE contentId = :contentId")
    fun existSubscription(contentId: Int): SubscriptionDao?
    @Query("SELECT * FROM Subscription")
    fun getListSubscription(): List<SubscriptionDao>
    //Recent
    @Insert
    fun addRecent(recent: RecentDao)
    @Query("DELETE FROM Recent WHERE id = :id")
    fun removeRecent(id: Int)
    @Update
    fun updateRecent(recent: RecentDao)
    @Query("SELECT * FROM Recent WHERE contentId = :id AND contentType = :contentType")
    fun getRecentById(id: Int, contentType: String): RecentDao?

    //Notification
    @Insert
    fun addNotification(notification: NotificationDao)
    @Query("DELETE FROM Notification WHERE id = :id")
    fun removeNotification(id: Int)

}