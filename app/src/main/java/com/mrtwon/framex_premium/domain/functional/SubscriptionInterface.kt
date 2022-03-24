package com.mrtwon.framex_premium.domain.functional

interface SubscriptionInterface<T> {
    interface Observer<T>{
        fun onChange(t: T)
    }

    fun observe(observer: Observer<T>)

    fun clear()
}