package com.mrtwon.framex_premium.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

    @Query("SELECT * FROM Favorite")
    fun getFavorite(): List<Favorite>

    @Query("SELECT * FROM Favorite WHERE id_content = :id")
    fun getFavoriteIdLiveData(id: Int): LiveData<Favorite>

    @Query("SELECT * FROM Favorite WHERE id_content = :id AND content_type = :contentType")
    fun getFavoriteElement(id: Int, contentType: String): Favorite?


    @Insert
    fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE id_content = :id AND content_type = :contentType")
    fun deleteFavoriteElement(id: Int, contentType: String)

    @Delete
    fun deleteFavorite(favorite: Favorite)


    @Query("SELECT * FROM Favorite")
    fun getFavoriteLiveData(): LiveData<List<Favorite>>


    @Insert
    fun insertRecent(recent: Recent)

    @Query("DELETE FROM Recent WHERE id = :id")
    fun deleteRecent(id: Int)

    @Update
    fun updateRecent(recent: Recent)

    @Query("SELECT * FROM Recent WHERE id_content = :idRef and content_type = :contentType")
    fun getRecentElement(idRef: Int, contentType: String): Recent?

    @Query("SELECT * FROM Recent ORDER BY time DESC")
    fun getRecentList(): List<Recent>


    // function for subscription table
    @Query("SELECT * FROM Subscription")
    fun getSubscriptions(): List<Subscription>
    @Query("SELECT * FROM Subscription WHERE content_id = :id")
    fun getSubscriptionById(id: Int): Subscription?
    @Query("SELECT * FROM Subscription WHERE content_id = :id")
    fun getSubscriptionByIdLiveData(id: Int): LiveData<Subscription>
    @Query("SELECT * FROM Subscription")
    fun getSubscriptionsLiveData(): LiveData<List<Subscription>>

    @Insert
    fun addSubscription(subscription: Subscription)

    @Update
    fun updateSubscription(subscription: Subscription)


    @Delete
    fun deleteSubscription(subscription: Subscription)

    @Query("DELETE FROM subscription WHERE content_id = :id")
    fun deleteSubscriptionById(id: Int)

    // function for notification table
    @Query("SELECT * FROM Notification")
    fun getNotification(): List<Notification>

    @Query("SELECT * FROM Notification")
    fun getNotificationLiveData(): LiveData<List<Notification>>

    @Insert
    fun insertNotifications(notification: List<Notification>)

    @Insert
    fun insertNotification(notification: Notification)

    @Delete
    fun deleteNotification(notification: Notification)
}