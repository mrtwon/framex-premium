package com.mrtwon.framex_premium

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mrtwon.framex_premium.ActivityAuth.AuthActivity
import com.mrtwon.framex_premium.ActivityWelcome.ActivityWelcome
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.File

class StartedActivity: AppCompatActivity() {
    val generalVM  = GeneralVM()
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.empty)
        Log.i("self-started", "db existing - ${isExist()}")
        if(isExist()){
            generalVM.model.isAuth({
                if(it)
                    startActivity(Intent(this, MainActivity::class.java))
                else
                    startActivity(Intent(this, AuthActivity::class.java))
                finish()
            },{
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
        }else{
            startActivity(Intent(this, ActivityWelcome::class.java))
            finish()
        }
        //startActivity(Intent(this, ActivityWelcome::class.java))
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("SdCardPath")
    fun isExist(): Boolean{
        val file = File("/data/data/com.mrtwon.framex/databases/database")
        return file.exists()
    }
}