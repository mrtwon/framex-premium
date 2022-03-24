package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetLiveDataSubscription  @Inject constructor(val databaseRepository: DatabaseRepository)
    : UseCase<SubscriptionInterface<List<Subscription>>, Unit>() {
    override suspend fun run(params: Unit): Either<Failure, SubscriptionInterface<List<Subscription>>> {
        return databaseRepository.getSubscriptionLiveData()
    }
}