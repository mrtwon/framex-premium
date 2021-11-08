package com.mrtwon.framex_premium

import android.app.Application
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import androidx.work.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.mrtwon.framex_premium.ActivityAuth.AuthActivity
import com.mrtwon.framex_premium.components.AppComponents
import com.mrtwon.framex_premium.WorkManager.Work
import com.mrtwon.framex_premium.components.DaggerAppComponents
import com.squareup.picasso.Downloader
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

class MyApplication: Application() {
    private val CACHE_SIZE = 52428800
    lateinit var appComponents: AppComponents
    lateinit var picasso: Picasso
    override fun onCreate() {
        appComponents = DaggerAppComponents.create()
        FirebaseApp.initializeApp(this)
        buildPicasso()
        statusWorker()
        getInstance = this
        //startWorkManager()
        //listenerAuthState()
        super.onCreate()
    }

    fun buildPicasso(){
        picasso = Picasso.Builder(this)
            .memoryCache(LruCache(CACHE_SIZE))
            .build()
    }

    private fun startWorkManager(){
        val instance = WorkManager.getInstance(applicationContext)

        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(Work::class.java, 30, TimeUnit.MINUTES)
            .addTag("subscription")
            .setConstraints(constraint)
            .build()

        instance.enqueueUniquePeriodicWork("subscription", ExistingPeriodicWorkPolicy.KEEP, myRequest)
    }

    private fun statusWorker(){
        val instance = WorkManager.getInstance(applicationContext)
        val workInfo = instance.getWorkInfosByTag("subscription")
        workInfo.get().forEach { worker -> log(worker.state.toString()) }
    }

    private fun listenerAuthState(){
        DaggerAppComponents.create().getFirebaseAuth().addAuthStateListener { state ->
            if(state.currentUser == null){
                startActivity(Intent(this, AuthActivity::class.java).apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    private fun log(msg: String){
        Log.i("self-application", msg)
    }

    companion object{
        lateinit var getInstance: MyApplication
    }
}