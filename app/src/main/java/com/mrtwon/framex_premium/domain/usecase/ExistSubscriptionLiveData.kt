package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class ExistSubscriptionLiveData @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<SubscriptionInterface<Boolean>, ExistSubscriptionLiveData.Param>() {
    data class Param(val contentId: Int)

    override suspend fun run(params: Param): Either<Failure, SubscriptionInterface<Boolean>> {
        return databaseRepository.existSubscriptionLiveData(param = params)
    }
}