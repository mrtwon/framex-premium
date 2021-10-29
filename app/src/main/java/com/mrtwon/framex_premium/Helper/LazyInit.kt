package com.mrtwon.framex_premium

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun <T: View> Activity.bindView(@IdRes id: Int): Lazy<T>{
    return lazyUnsychronized { findViewById(id) }
}

fun <T> lazyUnsychronized(init : () -> T) : Lazy<T>{
    return lazy(LazyThreadSafetyMode.NONE, init)
}