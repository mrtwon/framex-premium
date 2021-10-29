package com.mrtwon.framex_premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrtwon.framex_premium.Notification.createNotification
import kotlinx.android.synthetic.main.test_layout.view.*

class TestFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.test_layout, container, false)
        view.send.setOnClickListener{testSendNotification()}
        return view
    }

    fun testSendNotification(){
        createNotification(message = "Test Send", title = "Test", channelName = "Test Channel", context = requireContext())
    }
}