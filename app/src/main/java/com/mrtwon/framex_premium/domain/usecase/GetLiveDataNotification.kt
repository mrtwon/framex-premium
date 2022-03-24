package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetLiveDataNotification @Inject constructor(val databaseRepository: DatabaseRepository)
    : UseCase<SubscriptionInterface<List<Notification>>, Unit>() {
    override suspend fun run(params: Unit): Either<Failure, SubscriptionInterface<List<Notification>>> {
        return databaseRepository.getNotificationLiveData()
    }
}