package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetSubscriptionList @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<List<Subscription>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<Subscription>> {
        return databaseRepository.getSubscriptionList()
    }
}