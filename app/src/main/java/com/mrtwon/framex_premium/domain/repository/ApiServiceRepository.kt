package com.mrtwon.framex_premium.domain.repository

import com.mrtwon.framex_premium.domain.entity.EpisodeCount
import com.mrtwon.framex_premium.domain.entity.EpisodeOfSerial
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.usecase.GetAllEpisodeOfSerial
import com.mrtwon.framex_premium.domain.usecase.GetEpisodeCount

interface ApiServiceRepository {
    fun getEpisodeCount(param: GetEpisodeCount.Param): Either<Failure, EpisodeCount>
    fun getAllEpisodeOfSerial(param: GetAllEpisodeOfSerial.Param): Either<Failure, EpisodeOfSerial>
}