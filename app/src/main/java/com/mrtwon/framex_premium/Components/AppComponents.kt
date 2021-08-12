package com.mrtwon.framex_premium.Components

import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Model.Model
import com.mrtwon.framex_premium.Model.ModelApi
import com.mrtwon.framex_premium.Modules.ApiModule
import com.mrtwon.framex_premium.Modules.ModuleModel
import com.mrtwon.framex_premium.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.room.Database
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleModel::class, ApiModule::class])
abstract class AppComponents {
    abstract fun inject(vm: GeneralVM)

    abstract fun getNewModel(): ModelApi

    abstract fun getModel(): Model

    abstract fun getDatabase(): Database

    abstract fun getRatingApi(): XmlParse

    abstract fun getVideoCdnApi(): VideoCdnApi

    abstract fun getKinopiskApi(): KinopoiskApi
}