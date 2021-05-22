package com.konex.extra

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.Nullable
import com.konex.activity.MainActivity
import com.konex.R

class FloatingWindow: Service() {
    // variables
    private var mWindowManager: WindowManager? = null
    private var mVibrator: Vibrator? = null
    private var mPaperParams: WindowManager.LayoutParams? = null
    private var mRecycleBinParams: WindowManager.LayoutParams? = null
    private var mRecycleBinParamsRed: WindowManager.LayoutParams? = null
    private var windowHeight = 0
    private var windowWidth = 0
    // UI
    private var ivCrumpledPaper: ImageView? = null
    private var ivRecycleBin: ImageView? = null
    private var ivRecyclerBinRed: ImageView?=null

    // methods
    override fun onCreate() {
        super.onCreate()
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showHud()
        return START_STICKY
    }

    private fun showHud() {
        if (ivCrumpledPaper != null) {
            mWindowManager!!.removeView(ivCrumpledPaper)
            ivCrumpledPaper = null
        }
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        mPaperParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val displaymetrics = DisplayMetrics()
        mWindowManager!!.defaultDisplay.getMetrics(displaymetrics)
        windowHeight = displaymetrics.heightPixels
        windowWidth = displaymetrics.widthPixels
        mPaperParams!!.gravity = Gravity.TOP or Gravity.RIGHT
        ivCrumpledPaper = ImageView(this)
        ivCrumpledPaper!!.setImageResource(R.drawable.floating_icon)
        mPaperParams!!.x = 0
        mPaperParams!!.y = 100
        mWindowManager!!.addView(ivCrumpledPaper, mPaperParams)

        addCrumpledPaperOnTouchListener()
    }

    private fun addCrumpledPaperOnTouchListener() {
        ivCrumpledPaper!!.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = mPaperParams!!.x
                        initialY = mPaperParams!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        // add recycle bin when moving crumpled paper
                        addRecycleBinView()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val diffPosicaoX = (event.rawX - initialTouchX).toInt()
                        val diffPosicaoY = (event.rawY - initialTouchY).toInt()

                        val singleClick: Boolean = diffPosicaoX < 5 && diffPosicaoY < 5

                        if (singleClick) {
                            val intent = Intent(this@FloatingWindow, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            stopSelf()
                        }
                        val centerOfScreenByX = windowWidth / 2
                        // remove crumpled paper when the it is in the recycle bin's area

                        if (mPaperParams!!.y > windowHeight - ivRecycleBin!!.height - ivCrumpledPaper!!.height &&
                            mPaperParams!!.x > centerOfScreenByX - ivRecycleBin!!.width - ivCrumpledPaper!!.width / 2 && mPaperParams!!.x < centerOfScreenByX + ivRecycleBin!!.width / 2
                        ) {
                            mVibrator!!.vibrate(100)
                            stopSelf()
                        }


                        // always remove recycle bin ImageView when paper is dropped
                        mWindowManager!!.removeView(ivRecycleBin)
                        ivRecycleBin = null
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // move paper ImageView
                        mPaperParams!!.x = initialX + (initialTouchX - event.rawX).toInt()
                        mPaperParams!!.y = initialY + (event.rawY - initialTouchY).toInt()
                        mWindowManager!!.updateViewLayout(ivCrumpledPaper, mPaperParams)

                        val centerOfScreenByX = windowWidth / 2
                        if (mPaperParams!!.y > windowHeight - ivRecycleBin!!.height - ivCrumpledPaper!!.height &&
                            mPaperParams!!.x > centerOfScreenByX - ivRecycleBin!!.width - ivCrumpledPaper!!.width / 2 && mPaperParams!!.x < centerOfScreenByX + ivRecycleBin!!.width / 2
                        ) {
                            if (ivRecycleBin != null) {
                                ivRecycleBin!!.setImageResource(R.drawable.ic_floating_cancel_red)
                            }
                        }
                        else{
                            ivRecycleBin!!.setImageResource(R.drawable.ic_floting_cancel)
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun addRecycleBinView() {
        // add recycle bin ImageView centered on the bottom of the screen

        val RECYCLER_LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        mRecycleBinParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            RECYCLER_LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        mRecycleBinParams!!.gravity = Gravity.BOTTOM or Gravity.CENTER
        ivRecycleBin = ImageView(this)
        ivRecycleBin!!.setImageResource(R.drawable.ic_floting_cancel)
        mRecycleBinParams!!.x = 0
        mRecycleBinParams!!.y = 0
        mWindowManager!!.addView(ivRecycleBin, mRecycleBinParams)
    }


    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()

        // remove views on destroy!
        if (ivCrumpledPaper != null) {
            mWindowManager!!.removeView(ivCrumpledPaper)
            ivCrumpledPaper = null
        }
        if (ivRecycleBin != null) {
            mWindowManager!!.removeView(ivRecycleBin)
            ivRecycleBin = null
        }
    }

    companion object {
        // constants
        val BASIC_TAG = FloatingWindow::class.java.name

        // get intent methods
        fun getIntent(context: Context?): Intent {
            return Intent(context, FloatingWindow::class.java)
        }

    }
}