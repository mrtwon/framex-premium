package com.mrtwon.framex_premium.screen.mainActivity

import androidx.navigation.NavController

interface MainActivityCallback {
    fun getNavController(): NavController
    fun hiddenBottomBar()
    fun showBottomBar()
}