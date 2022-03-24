package com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource

import com.mrtwon.framex_premium.domain.entity.EpisodeCount
import com.mrtwon.framex_premium.domain.entity.EpisodeOfSerial
import com.mrtwon.framex_premium.domain.entity.OneEpisode
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.usecase.GetAllEpisodeOfSerial
import com.mrtwon.framex_premium.domain.usecase.GetEpisodeCount
import retrofit2.Retrofit
import javax.inject.Inject

class VideoCdnDataSource @Inject constructor(val videoCdnApi: VideoCdnApi) {
    fun getEpisodeCount(param: GetEpisodeCount.Param): Either<Failure, EpisodeCount> {
        val response = videoCdnApi.getTvContentById(param.id).execute()
        if(response.isSuccessful && response.body() != null){
            val contentResponse = response.body()
            val episodeCount = contentResponse!!.data[0].episodeCount
            val id = contentResponse.data[0].id
            val kpId = contentResponse.data[0].kinopoiskId
            val imdbId = contentResponse.data[0].imdbId
            return Either.Right(
                EpisodeCount(
                    id = id,
                    kpId = kpId,
                    imdbId = imdbId,
                    episodeCount = episodeCount
                )
            )
        }else{
            return Either.Left(Failure.ServerError)
        }
    }
    fun getAllEpisodeOfSerial(param: GetAllEpisodeOfSerial.Param): Either<Failure, EpisodeOfSerial>{
        val response = videoCdnApi.getTvContentById(param.id).execute()
        val id = response.body()?.data?.get(0)?.id ?: return Either.Left(Failure.ServerError)
        val oneEpisodeList = arrayListOf<OneEpisode>()
        if(response.isSuccessful && response.body()?.data?.get(0)?.episodes != null){
            val contentResponse = response.body()!!.data[0].episodes
            for(content in contentResponse){
                oneEpisodeList.add(
                    OneEpisode(season = content.seasonNum.toString(), episode = content.num)
                )
            }
        }
        return Either.Right(
            EpisodeOfSerial(idContent = id, allEpisodeCount = oneEpisodeList.size, list = oneEpisodeList)
        )
    }
}