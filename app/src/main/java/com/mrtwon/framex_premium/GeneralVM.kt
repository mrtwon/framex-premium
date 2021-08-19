package com.mrtwon.framex_premium

import androidx.lifecycle.ViewModel
import com.mrtwon.framex_premium.Model.Model
import javax.inject.Inject

open class GeneralVM: ViewModel() {
    @Inject
    lateinit var model: Model
    init {
        MyApplication.getInstance.appComponents.inject(this)
    }
}