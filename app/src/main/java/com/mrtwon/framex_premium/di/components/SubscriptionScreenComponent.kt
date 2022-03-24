package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.SubscriptionScreenScope
import com.mrtwon.framex_premium.screen.fragmentSubscription.FragmentSubscription
import dagger.Subcomponent

@SubscriptionScreenScope
@Subcomponent
interface SubscriptionScreenComponent {
    fun inject(fragment: FragmentSubscription)
}