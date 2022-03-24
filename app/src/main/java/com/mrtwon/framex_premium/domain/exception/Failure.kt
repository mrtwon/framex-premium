package com.mrtwon.framex_premium.domain.exception

sealed class Failure {
    object NetworkConnection: Failure()
    object ServerError: Failure()
    object ClientError: Failure()
}