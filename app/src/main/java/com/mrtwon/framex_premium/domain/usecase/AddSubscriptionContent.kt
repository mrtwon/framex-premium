package com.mrtwon.framex_premium.domain.usecase

import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.EpisodeCount
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.repository.ApiServiceRepository
import com.mrtwon.framex_premium.domain.repository.ContentRepository
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddSubscriptionContent @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val apiServiceRepository: ApiServiceRepository,
    val contentRepository: ContentRepository
    ): UseCase<Unit, AddSubscriptionContent.Param>() {
    data class Param(val id: Int)
    data class SecondaryParam(val subscription: Subscription)

    override suspend fun run(params: Param): Either<Failure, Unit> {

        val contentResult = contentRepository.getContentById(
            GetContentById.Params(id = params.id, content = ContentEnum.Serial)
        )
        if (contentResult.isLeft) {
            return contentResult as Either.Left<Failure>
        }
        val contentResultRight = (contentResult as Either.Right<Content?>).b
            ?: return Either.Left<Failure>(Failure.ServerError)

        val resultEpisodeCount = apiServiceRepository.getEpisodeCount(
            GetEpisodeCount.Param(params.id, ContentEnum.Serial)
        )
        if (resultEpisodeCount.isLeft) {
            return resultEpisodeCount as Either.Left<Failure>
        }
        val resultEpisodeCountRight = resultEpisodeCount as Either.Right<EpisodeCount>

        return databaseRepository.addSubscription(
            AddSubscriptionContent.SecondaryParam(
                Subscription(
                    //id = contentResultRight.id ?: return Either.Left<Failure>(Failure.ServerError),
                    contentId = contentResultRight.id
                        ?: return Either.Left<Failure>(Failure.ServerError),
                    posterPreview = contentResultRight.posterPreview,
                    ruTitle = contentResultRight.ruTitle,
                    episodeCount = resultEpisodeCount.b.episodeCount
                )
            )
        )
    }
}