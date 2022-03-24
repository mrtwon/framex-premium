package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.AboutScreenScope
import com.mrtwon.framex_premium.screen.fragmentAbout.FragmentAboutMovie
import com.mrtwon.framex_premium.screen.fragmentAbout.FragmentAboutSerial
import dagger.Subcomponent

@AboutScreenScope
@Subcomponent
interface AboutScreenComponent {
    fun inject(aboutMovie: FragmentAboutMovie)
    fun inject(aboutSerial: FragmentAboutSerial)
}