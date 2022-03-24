package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject

class ExistFavorite @Inject constructor(val databaseRepository: DatabaseRepository): UseCase<Boolean, ExistFavorite.Param>() {
    data class Param(val contentId: Int, val contentEnum: ContentEnum)

    override suspend fun run(params: Param): Either<Failure, Boolean> {
        return databaseRepository.existFavorite(param = params)
    }
}