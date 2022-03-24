package com.mrtwon.framex_premium.screen.fragmentTop

import com.mrtwon.framex_premium.domain.entity.Content

interface TopOpenCallback {
    fun onOpen(content: Content)
}