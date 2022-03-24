package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.EpisodeOfSerial
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ApiServiceRepository
import javax.inject.Inject

class GetAllEpisodeOfSerial @Inject constructor(val apiServiceRepository: ApiServiceRepository): UseCase<EpisodeOfSerial, GetAllEpisodeOfSerial.Param>() {
    data class Param(val id: Int)

    override suspend fun run(params: Param): Either<Failure, EpisodeOfSerial> {
        return apiServiceRepository.getAllEpisodeOfSerial(params)
    }
}