package com.mrtwon.framex_premium.data.repository

import android.util.Log
import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.NotificationDao
import com.mrtwon.framex_premium.data.entity.RecentDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao
import com.mrtwon.framex_premium.data.localDataSource.Database
import com.mrtwon.framex_premium.data.localDataSource.liveData.*
import com.mrtwon.framex_premium.domain.entity.Favorite
import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import com.mrtwon.framex_premium.domain.usecase.*
import java.lang.Exception
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(val database: Database): DatabaseRepository {


    override fun getFavoriteLiveData(): Either<Failure, SubscriptionInterface<List<Favorite>>> {
        return request {
            LiveDataFavoriteContent(
                database
                    .roomDao()
                    .getFavoriteLiveData()
            )
        }
    }

    override fun getSubscriptionLiveData(): Either<Failure, SubscriptionInterface<List<Subscription>>> {
        return request {
            LiveDataSubscriptionContent(
                database
                    .roomDao()
                    .getSubscriptionLiveData()
            )
        }
    }

    override fun getRecentLiveData(): Either<Failure, SubscriptionInterface<List<Recent>>> {
        return request {
            LiveDataRecentContent(
                database
                    .roomDao()
                    .getRecentLiveData()
            )
        }
    }

    override fun getNotificationLiveData(): Either<Failure, SubscriptionInterface<List<Notification>>> {
        return request {
            LiveDataNotificationContent(
                database
                    .roomDao()
                    .getNotificationLiveData()
            )
        }
    }

    override fun getSubscriptionList(): Either<Failure, List<Subscription>> {
        return requestList<SubscriptionDao, Subscription>(
            { database.roomDao().getListSubscription() },
            transform = {
                val resultList = arrayListOf<Subscription>()
                it.forEach{ resultList.add(it.toSubscription()) }
                resultList
            }
        )

    }


    override fun addFavorite(param: AddFavoriteContent.SecondaryParam): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .addFavorite(
                    FavoriteDao.fromFavorite(
                        param.favorite
                    )
                )
        }
    }

    override fun removeFavorite(param: RemoveFavoriteContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .removeFavorite(
                    id = param.id,
                    contentType = param.contentEnum.type
                )
        }
    }

    override fun addSubscription(param: AddSubscriptionContent.SecondaryParam): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .addSubscription(
                    SubscriptionDao.fromSubscription(param.subscription).apply {
                        this.episodeCount -= 1
                    }
                )
        }
    }


    override fun removeSubscription(param: RemoveSubscription.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .removeSubscription(
                    param.id
                )
        }
    }

    override fun updateSubscription(param: UpdateSubscriptionContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .updateSubscription(
                    SubscriptionDao.fromSubscription(
                        param.subscription
                    )
                )
        }
    }

    override fun addRecent(param: AddRecentContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .addRecent(
                    RecentDao.fromRecent(
                        param.recent
                    )
                )
        }
    }

    override fun removeRecent(param: RemoveRecentContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .removeRecent(
                    param.id
                )
        }
    }

    override fun updateRecent(param: UpdateRecentContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .updateRecent(
                    RecentDao.fromRecent(
                        param.recent
                    )
                )
        }
    }

    override fun addNotification(param: AddNotificationContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .addNotification(
                    NotificationDao.fromNotification(
                        param.notification
                    )
                )
        }
    }

    override fun removeNotification(param: RemoveNotificationContent.Param): Either<Failure, Unit> {
        return request {
            database
                .roomDao()
                .removeNotification(
                    param.id
                )
        }
    }

    override fun existSubscriptionLiveData(param: ExistSubscriptionLiveData.Param): Either<Failure, SubscriptionInterface<Boolean>> {
        return request {
            LiveDataExisting(
                database
                    .roomDao()
                    .existSubscriptionLiveData(param.contentId)
            )
        }
    }

    override fun existSubscription(param: ExistSubscription.Param): Either<Failure, Boolean> {
        return request {
            database
                .roomDao()
                .existSubscription(param.contentId) != null
        }
    }

    override fun existFavoriteLiveData(param: ExistFavoriteLiveData.Param): Either<Failure, SubscriptionInterface<Boolean>> {
        return request {
            LiveDataExisting(
                database
                    .roomDao()
                    .existFavoriteLiveData(param.contentId, param.contentEnum.type)
            )
        }
    }

    override fun existFavorite(param: ExistFavorite.Param): Either<Failure, Boolean> {
        return request {
            database
                .roomDao()
                .existFavorite(param.contentId, param.contentEnum.type) != null
        }
    }

    override fun getRecentById(param: GetRecentById.Param): Either<Failure, Recent?> {
        return request {
            Recent.fromRecentDao(
                database
                    .roomDao()
                    .getRecentById(param.contentId, param.contentEnum.type)
            )
        }
    }


    private fun <T> request (
        call: () -> T
    ): Either<Failure, T> {
        return try{
            val result = call()
            Log.i("self-favorite", "successful  favorite act")
            Either.Right(result)
        }catch (e: Exception){
            Log.i("self-favorite", "exception favorite")
            e.printStackTrace()
            Either.Left(Failure.ClientError)
        }
    }
    private fun <T, R> requestList(
        call: () -> List<T>,
        transform: (List<T>) -> List<R>
    ): Either<Failure, List<R>>{
        return try {
            val result = call()
            Either.Right(transform(result))
        }catch (e: Exception){
            Either.Left(Failure.ClientError)
        }
    }
}