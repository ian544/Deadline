package com.example.deadline

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat

private const val DEBUG_TAG = "Gestures"

class MainActivity :
    Activity(),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private lateinit var mDetector: GestureDetectorCompat

    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener.
        mDetector = GestureDetectorCompat(this, this)
        // Set the gesture detector as the double-tap
        // listener.
        mDetector.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // Check if e1 and e2 are not null
        if (e1 == null) return false

        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y

        // Determine if the swipe is horizontal or vertical
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Horizontal swipe
            if (diffX > 0) {
                // Right swipe
                Log.d(DEBUG_TAG, "Swipe Right")
            } else {
                // Left swipe
                Log.d(DEBUG_TAG, "Swipe Left")
            }
        } else {
            // Vertical swipe
            if (diffY > 0) {
                // Down swipe
                Log.d(DEBUG_TAG, "Swipe Down")
            } else {
                // Up swipe
                Log.d(DEBUG_TAG, "Swipe Up")
            }
        }
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onLongPress: $event")
    }

    override fun onScroll(
        p0: MotionEvent?,
        event1: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(DEBUG_TAG, "onScroll: $event1")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onShowPress: $event")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapUp: $event")
        return true
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTap: $event")
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: $event")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: $event")
        return true
    }

}