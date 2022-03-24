package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.SearchScreenScope
import com.mrtwon.framex_premium.screen.fragmentSearch.searchByDescription.FragmentSearchDescription
import com.mrtwon.framex_premium.screen.fragmentSearch.searchByTitle.FragmentSearchTitle
import dagger.Subcomponent

@SearchScreenScope
@Subcomponent
interface SearchScreenComponent {
    fun inject(searchByTitle: FragmentSearchTitle)
    fun inject(searchByDescription: FragmentSearchDescription)
}