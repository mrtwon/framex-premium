package com.mrtwon.framex_premium.di

import android.content.Context
import com.mrtwon.framex_premium.di.scope.AppScope
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://videocdn.tv/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun provideOkHttp(): OkHttpClient{
        return OkHttpClient
            .Builder()
            .build()
    }

    @AppScope
    @Provides
    fun providePicasso(context: Context): Picasso {
        return Picasso.Builder(context)
            .memoryCache(LruCache(52428800))
            .build()
    }
}