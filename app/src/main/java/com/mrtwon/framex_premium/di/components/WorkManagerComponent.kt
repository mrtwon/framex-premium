package com.mrtwon.framex_premium.di.components

import com.mrtwon.framex_premium.di.scope.WorkManagerScope
import com.mrtwon.framex_premium.workManager.Work
import dagger.Subcomponent

@WorkManagerScope
@Subcomponent
interface WorkManagerComponent {
    fun inject(worker: Work)
}