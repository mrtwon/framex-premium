package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.TopScreenScope
import com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear.FragmentTopCurrentYear
import com.mrtwon.framex_premium.screen.fragmentTop.topGenres.FragmentTopGenres
import dagger.Subcomponent

@TopScreenScope
@Subcomponent
interface TopScreenComponent {
    fun inject(fragment: FragmentTopCurrentYear)
    fun inject(fragment: FragmentTopGenres)
}