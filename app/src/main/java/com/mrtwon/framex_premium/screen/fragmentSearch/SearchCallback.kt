package com.mrtwon.framex_premium.screen.fragmentSearch

import com.mrtwon.framex_premium.domain.entity.Content

interface SearchCallback {
    fun onOpen(content: Content)
}