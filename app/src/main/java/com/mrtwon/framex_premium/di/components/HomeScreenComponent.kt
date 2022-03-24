package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.HomeScreenScope
import com.mrtwon.framex_premium.screen.fragmentHome.FragmentHome
import dagger.Subcomponent

@HomeScreenScope
@Subcomponent()
interface HomeScreenComponent {
    fun inject(fragment: FragmentHome)
}