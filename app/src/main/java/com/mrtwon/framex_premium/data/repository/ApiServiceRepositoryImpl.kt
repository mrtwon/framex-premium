package com.mrtwon.framex_premium.data.repository

import com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource.VideoCdnDataSource
import com.mrtwon.framex_premium.domain.entity.EpisodeCount
import com.mrtwon.framex_premium.domain.entity.EpisodeOfSerial
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ApiServiceRepository
import com.mrtwon.framex_premium.domain.usecase.GetAllEpisodeOfSerial
import com.mrtwon.framex_premium.domain.usecase.GetEpisodeCount
import javax.inject.Inject

class ApiServiceRepositoryImpl @Inject constructor(val videoCdnDataSource: VideoCdnDataSource):
    ApiServiceRepository {
    override fun getEpisodeCount(param: GetEpisodeCount.Param): Either<Failure, EpisodeCount> {
        return videoCdnDataSource.getEpisodeCount(param)
    }

    override fun getAllEpisodeOfSerial(param: GetAllEpisodeOfSerial.Param): Either<Failure, EpisodeOfSerial> {
        return videoCdnDataSource.getAllEpisodeOfSerial(param = param)
    }
}