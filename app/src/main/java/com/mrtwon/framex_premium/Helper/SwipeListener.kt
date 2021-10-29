package com.mrtwon.framex_premium.Helper

import android.annotation.SuppressLint
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.mrtwon.framex_premium.MyApplication
import java.lang.Exception
import kotlin.math.log

class SwipeListener(val right: () -> Unit = {}, val left: () -> Unit = {}, val top: () -> Unit = {}, val bottom: () -> Unit = {}) : View.OnTouchListener{

    private val gestureDetector = GestureDetector(MyApplication.getInstance.applicationContext, GestureListener())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.actionMasked){
            MotionEvent.ACTION_BUTTON_PRESS -> log("ACTION_BUTTON_PRESS")
            MotionEvent.ACTION_BUTTON_RELEASE -> log("ACTION BUTTON RELEASE")
            else -> log("OTHER. ${event?.actionMasked}")
        }
        gestureDetector.onTouchEvent(event)
        return true
    }

    fun log(s: String) = Log.i("self-swipe", s)

    inner class GestureListener : GestureDetector.SimpleOnGestureListener(){
        private val SWIPE_THRESHOLD = 50
        private val SWIPE_VELOCITY_THRESHOLD = 50

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                    result = true
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                }
                result = true
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
        private fun onSwipeRight() = right()
        private fun onSwipeLeft() = left()
        private fun onSwipeBottom() = bottom()
        private fun onSwipeTop() = top()

    }

}