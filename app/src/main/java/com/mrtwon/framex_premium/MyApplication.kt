package com.mrtwon.framex_premium

import android.app.Application
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import androidx.work.*
import com.google.firebase.FirebaseApp
import com.mrtwon.framex_premium.components.AppComponents
import com.mrtwon.framex_premium.WorkManager.Work
import com.mrtwon.framex_premium.components.DaggerAppComponents
import com.squareup.picasso.Downloader
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit

class MyApplication: Application() {
    private val CACHE_SIZE = 52428800
    lateinit var appComponents: AppComponents
    lateinit var picasso: Picasso
    override fun onCreate() {
        appComponents = DaggerAppComponents.create()
        getInstance = this
        startWorkManager()
        FirebaseApp.initializeApp(this)
        buildPicasso()
        super.onCreate()
    }

    fun buildPicasso(){
        picasso = Picasso.Builder(this)
            .memoryCache(LruCache(CACHE_SIZE))
            .build()
    }

    fun startWorkManager(){
        val instance = WorkManager.getInstance(applicationContext)
        val workInfo = instance.getWorkInfosByTag("subscription")
        if(workInfo.get().isEmpty()){
            Log.i("self-about","WokManager started")

            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myRequest = PeriodicWorkRequest.Builder(Work::class.java, 15, TimeUnit.MINUTES)
                .addTag("subscription")
                .setConstraints(constraint)
                .build()

            instance.enqueue(myRequest)

        }
    }
    fun sendStatic(){
        appComponents.getModel().sendStatic()
    }

    companion object{
        lateinit var getInstance: MyApplication
    }
}