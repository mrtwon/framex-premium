package com.mrtwon.testfirebase.paging

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor: Executor {
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable?) {
        command?.let { handler.post(it) }
    }
}