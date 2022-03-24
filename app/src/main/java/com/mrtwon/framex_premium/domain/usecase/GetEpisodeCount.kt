package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.EpisodeCount
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ApiServiceRepository
import javax.inject.Inject

class GetEpisodeCount @Inject constructor(val apiServiceRepository: ApiServiceRepository): UseCase<EpisodeCount, GetEpisodeCount.Param>() {
    data class Param(val id: Int, val contentType: ContentEnum)

    override suspend fun run(params: Param): Either<Failure, EpisodeCount> {
        return apiServiceRepository.getEpisodeCount(params)
    }
}