package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.WebViewScreenScope
import com.mrtwon.framex_premium.screen.activityWebView.ActivityWebView
import dagger.Subcomponent

@WebViewScreenScope
@Subcomponent
interface WebViewScreenComponent {
    fun inject(activity: ActivityWebView)
}