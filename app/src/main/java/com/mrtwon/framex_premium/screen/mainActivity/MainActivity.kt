package com.mrtwon.framex_premium.screen.mainActivity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.bindView
import com.mrtwon.framex_premium.screen.dialogDonat.DialogDonate
import com.mrtwon.framex_premium.lazyUnsychronized

class MainActivity : AppCompatActivity(), MainActivityCallback {

    private val bottomBar by bindView<BottomNavigationView>(R.id.bottom_nav_menu)
    private val mNavController by lazyUnsychronized { Navigation.findNavController(this, R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        getSharedPreferences("StartApp", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val redirect = intent.getStringExtra("redirect")
        val action = intent.action
        val data = intent.dataString

        if(Intent.ACTION_VIEW.equals(action) && data != null){
            deepLinkAction(data)
        }

        if(redirect != null){
            when(redirect){
                "FragmentSubscription" -> mNavController.navigate(R.id.fragmentSubscription)
            }
        }

        bottomBar.setupWithNavController(mNavController)
        showDonatDialog()
    }

    private fun showDonatDialog(){
        val settings = getSharedPreferences("DonatSetting", Context.MODE_PRIVATE)
        val count = settings.getInt("startApp", 1)
        if((count % 4) == 0 || count == 1){
            val dialogDonate = DialogDonate()
            dialogDonate.show(supportFragmentManager, "donateDialog")
        }
    }

    override fun getNavController(): NavController {
        return mNavController
    }

    override fun hiddenBottomBar(){
        bottomBar.visibility = View.GONE
    }

    override fun showBottomBar(){
        bottomBar.visibility = View.VISIBLE
    }

    private fun deepLinkAction(data: String){
        val uriData = Uri.parse(data)
        val pathList = uriData.path?.split("/") ?: return
        if(pathList.size < 3) return
        val charContent = pathList[1]
        val idContent = pathList[2].toInt()
        val bundle = Bundle().apply {
            putInt("id", idContent)
        }
        when(charContent){
            "s" -> mNavController.navigate(R.id.fragmentAboutSerial, bundle)
            "m" -> mNavController.navigate(R.id.fragmentAboutMovie, bundle)
        }
    }



    private fun log(s: String){
        Log.i("self-main",s)
    }
}