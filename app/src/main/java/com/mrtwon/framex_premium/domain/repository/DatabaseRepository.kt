package com.mrtwon.framex_premium.domain.repository

import com.mrtwon.framex_premium.domain.entity.Favorite
import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.usecase.*

interface DatabaseRepository {
    fun getFavoriteLiveData(): Either<Failure, SubscriptionInterface<List<Favorite>>>
    fun getSubscriptionLiveData(): Either<Failure, SubscriptionInterface<List<Subscription>>>
    fun getRecentLiveData(): Either<Failure, SubscriptionInterface<List<Recent>>>
    fun getNotificationLiveData(): Either<Failure, SubscriptionInterface<List<Notification>>>
    fun getSubscriptionList(): Either<Failure, List<Subscription>>
    fun addFavorite(param: AddFavoriteContent.SecondaryParam): Either<Failure, Unit> // +
    fun removeFavorite(param: RemoveFavoriteContent.Param): Either<Failure, Unit> // +
    fun addSubscription(param: AddSubscriptionContent.SecondaryParam): Either<Failure, Unit>
    fun removeSubscription(param: RemoveSubscription.Param): Either<Failure, Unit> // +
    fun updateSubscription(param: UpdateSubscriptionContent.Param): Either<Failure, Unit> // +
    fun addRecent(param: AddRecentContent.Param): Either<Failure, Unit> // +
    fun removeRecent(param: RemoveRecentContent.Param): Either<Failure, Unit> // +
    fun updateRecent(param: UpdateRecentContent.Param): Either<Failure, Unit> // +
    fun addNotification(param: AddNotificationContent.Param): Either<Failure, Unit> // +
    fun removeNotification(param: RemoveNotificationContent.Param): Either<Failure, Unit> // +
    fun existSubscriptionLiveData(param: ExistSubscriptionLiveData.Param): Either<Failure, SubscriptionInterface<Boolean>>
    fun existSubscription(param: ExistSubscription.Param): Either<Failure, Boolean>
    fun existFavoriteLiveData(param: ExistFavoriteLiveData.Param): Either<Failure, SubscriptionInterface<Boolean>>
    fun existFavorite(param: ExistFavorite.Param): Either<Failure, Boolean>
    fun getRecentById(param: GetRecentById.Param): Either<Failure, Recent?>
}