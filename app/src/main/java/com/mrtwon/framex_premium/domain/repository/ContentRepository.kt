package com.mrtwon.framex_premium.domain.repository

import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.Either
import com.mrtwon.framex_premium.domain.usecase.GetContentById

interface ContentRepository {
    fun getContentById(request: GetContentById.Params): Either<Failure, Content?>
}