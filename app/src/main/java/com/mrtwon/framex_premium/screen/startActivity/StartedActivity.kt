package com.mrtwon.framex_premium.screen.startActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.screen.activityWelcome.ActivityWelcome
import java.io.File

class StartedActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.empty)
        Log.i("self-started", "db existing - ${isExist()}")
        if(isExist()){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, ActivityWelcome::class.java))
        }
        finish()
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("SdCardPath")
    fun isExist(): Boolean{
        val file = File("/data/data/com.mrtwon.framex/databases/database")
        return file.exists()
    }
}