package com.mrtwon.framex_premium.FragmentTop

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.Content.RatingEnum
import com.mrtwon.framex_premium.GeneralVM

class MainTopVM: GeneralVM() {
    val filterLiveData = MutableLiveData<Filter>(Filter(RatingEnum.Kinopoisk, null))
}