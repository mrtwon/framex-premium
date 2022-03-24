package com.mrtwon.framex_premium.di

import com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource.VideoCdnApi
import com.mrtwon.framex_premium.di.scope.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {

    @AppScope
    @Provides
    fun provideVideoCdnApi(retrofit: Retrofit): VideoCdnApi{
        return retrofit.create(VideoCdnApi::class.java)
    }
}