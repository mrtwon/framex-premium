package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.FavoriteScreenScope
import com.mrtwon.framex_premium.screen.fragmentFavorite.FragmentFavorite
import dagger.Subcomponent

@FavoriteScreenScope
@Subcomponent
interface FavoriteScreenComponent {
    fun inject(fragment: FragmentFavorite)
}