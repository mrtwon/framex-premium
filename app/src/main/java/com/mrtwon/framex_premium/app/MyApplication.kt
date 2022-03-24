package com.mrtwon.framex_premium.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.firebase.FirebaseApp
import com.mrtwon.framex_premium.di.AppComponent
import com.mrtwon.framex_premium.di.DaggerAppComponent
import com.mrtwon.framex_premium.workManager.Work
import java.util.concurrent.TimeUnit

class MyApplication: Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        incrementCountStartApp()
        startWorkManager()
        appComponent = DaggerAppComponent
            .builder()
            .addContent(applicationContext)
            .build()

        getInstance = this
        super.onCreate()
    }

    /*fun oneTimeWorker(){
        val instance = WorkManager.getInstance(applicationContext)
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myRequest = OneTimeWorkRequest.Builder(Work::class.java)
            .addTag("subscription-test")
            .build()

        instance.enqueue(myRequest)
    }*/

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

    /*private fun statusWorker(){
        val instance = WorkManager.getInstance(applicationContext)
        val workInfo = instance.getWorkInfosByTag("subscription")
        workInfo.get().forEach { worker -> log(worker.state.toString()) }
    }*/

    private fun incrementCountStartApp(){
        val settings = getSharedPreferences("DonatSetting", MODE_PRIVATE)
        val editor = settings.edit()
        val newCountStart = settings.getInt("startApp", 0) + 1
        editor.putInt("startApp", newCountStart)
        editor.apply()
    }



private fun log(msg: String){
   Log.i("self-application", msg)
}

companion object{
   lateinit var getInstance: MyApplication
}
}
val Context.appComponent: AppComponent
get() = when(this){
    is MyApplication -> appComponent
    else -> this.applicationContext.appComponent
}