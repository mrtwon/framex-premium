package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetRecentById @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Recent?, GetRecentById.Param>() {
    data class Param(val contentId: Int, val contentEnum: ContentEnum)

    override suspend fun run(params: Param): Either<Failure, Recent?> {
        return databaseRepository.getRecentById(param = params)
    }
}