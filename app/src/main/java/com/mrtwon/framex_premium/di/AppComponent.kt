package com.mrtwon.framex_premium.di

import android.content.Context
import com.mrtwon.framex_premium.di.components.*
import com.mrtwon.framex_premium.di.scope.AppScope
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.Provides

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        ApiModule::class,
        FirebaseModule::class,
        DatabaseModule::class,
        BindsModule::class
    ]
)
interface AppComponent {
    fun createFavoriteComponent(): FavoriteScreenComponent
    fun createHomeComponent(): HomeScreenComponent
    fun createSubscriptionComponent(): SubscriptionScreenComponent
    fun createTopComponent(): TopScreenComponent
    fun createWebViewComponent(): WebViewScreenComponent
    fun createWorkManagerComponent(): WorkManagerComponent
    fun createContentAboutComponent(): AboutScreenComponent
    fun createSearchComponent(): SearchScreenComponent
    @Component.Builder
    interface AppBuilder{
        fun build(): AppComponent
        @BindsInstance
        fun addContent(context: Context): AppBuilder
    }
}