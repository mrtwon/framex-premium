package com.mrtwon.framex_premium.di

import com.mrtwon.framex_premium.data.repository.ApiServiceRepositoryImpl
import com.mrtwon.framex_premium.data.repository.ContentRepositoryImpl
import com.mrtwon.framex_premium.data.repository.DatabaseRepositoryImpl
import com.mrtwon.framex_premium.data.repository.PagingRepositoryImpl
import com.mrtwon.framex_premium.di.scope.AppScope
import com.mrtwon.framex_premium.domain.repository.ApiServiceRepository
import com.mrtwon.framex_premium.domain.repository.ContentRepository
import com.mrtwon.framex_premium.domain.repository.DatabaseRepository
import com.mrtwon.framex_premium.domain.repository.PagingRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BindsModule {
    @AppScope
    @Binds
    abstract fun bindApiServiceRepository(apiServiceRepositoryImpl: ApiServiceRepositoryImpl): ApiServiceRepository
    @AppScope
    @Binds
    abstract fun bindContentRepository(contentRepositoryImpl: ContentRepositoryImpl): ContentRepository
    @AppScope
    @Binds
    abstract fun bindDatabaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl): DatabaseRepository
    @AppScope
    @Binds
    abstract fun bindPagingRepository(pagingRepositoryImpl: PagingRepositoryImpl): PagingRepository
}