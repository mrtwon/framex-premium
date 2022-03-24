package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class AddNotificationContent @Inject constructor(val databaseRepository: DatabaseRepository)
    : UseCase<Unit, AddNotificationContent.Param>() {
    data class Param(val notification: Notification)

    override suspend fun run(params: Param): Either<Failure, Unit> {
        return databaseRepository.addNotification(params)
    }

}