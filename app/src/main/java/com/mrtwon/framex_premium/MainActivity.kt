package com.mrtwon.framex_premium

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.mrtwon.framex_premium.FragmentAbout.FragmentAboutMovie
import com.squareup.picasso.Picasso
import java.net.URI

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomBar: BottomNavigationView
    val vm: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomBar = findViewById(R.id.bottom_nav_menu)
        //bottomBar.setOnItemSelectedListener(this)


        //если пришёл intent с намерением запустить фрагмент
        val redirect = intent.getStringExtra("redirect")
        val action = intent.action
        val data = intent.dataString
        if(Intent.ACTION_VIEW.equals(action) && data != null){
            deepLinkAction(data)
        }
        if(redirect != null){
            when(redirect){
                "FragmentSubscription" -> navController.navigate(R.id.fragmentSubscription)
            }
        }

        bottomBar.setupWithNavController(navController)
    }
    fun hiddenBottomBar(){
        bottomBar.visibility = View.GONE
    }
    fun showBottomBar(){
        bottomBar.visibility = View.VISIBLE
    }


    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> { navController.navigate(R.id.fragmentFavorite) }
            R.id.home -> { navController.navigate(R.id.fragmentHome)}
            R.id.search -> {  navController.navigate(R.id.fragmentSearch) }
            R.id.subscription -> { navController.navigate(R.id.fragmentSubscription) }
        }
        return true
    }*/
    fun reselectedNavigationPosition(){
        /*val current = navController.currentDestination?.id
        when(current){
            R.id.fragmentHome -> {
                if(bottomBar.selectedItemId != R.id.home) {
                    bottomBar.selectedItemId = R.id.home
                }
            }
            R.id.fragmentFavorite -> {
                if(bottomBar.selectedItemId != R.id.favorite) {
                    bottomBar.selectedItemId = R.id.favorite
                }
            }
            R.id.fragmentSearch -> {
                if(bottomBar.selectedItemId != R.id.search) {
                    bottomBar.selectedItemId = R.id.search
                }
            }
        }*/
    }


    fun deepLinkAction(data: String){
        val uriData = Uri.parse(data)
        val pathList = uriData.path?.split("/") ?: return
        if(pathList.size < 3) return
        val charContent = pathList[1]
        val idContent = pathList[2].toInt()
        val bundle = Bundle().apply {
            putInt("id", idContent)
        }
        when(charContent){
            "s" -> navController.navigate(R.id.fragmentAboutSerial, bundle)
            "m" -> navController.navigate(R.id.fragmentAboutMovie, bundle)
        }
    }

    private fun log(s: String){
        Log.i("self-main",s)
    }
}