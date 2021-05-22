package com.konex.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.konex.R
import com.konex.extra.FloatingWindow
import com.konex.model.BugReport
import kotlinx.android.synthetic.main.activity_bug_reporting_window.*


class BugReportingWindow : AppCompatActivity() {
    lateinit var userId: String
    lateinit var heightNWidth: String
    lateinit var appName: String
    private var position: Int = 0
    private var images: ArrayList<Uri?>? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    lateinit var bugInfo: BugReport
    val status = "Not Resolved"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_reporting_window)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        heightNWidth = "$width * $height"
        appName = intent.getStringExtra("appName").toString()
        bug_app_name.text = "Bug Report for $appName"
        database = FirebaseDatabase.getInstance()

        databaseReference = database.getReference("Bugs")
        userId = databaseReference.push().key.toString()
        bug_back.setOnClickListener {
            onBackPressed()
        }
        bug_submit.setOnClickListener {
            if (TextUtils.isEmpty(bug_tittle.text.toString())) {
                bug_tittle.error = "Enter Bug Tittle"
            } else if (TextUtils.isEmpty(bug_desc.text.toString())) {
                bug_tittle.error = "Enter Bug Description"
            } else {

                addDataTofirebase(
                    bug_tittle.text.toString(),
                    bug_desc.text.toString(),
                    getDeviceName(),
                    heightNWidth,
                    appName,
                    status
                )
                finish()
            }
        }

    }



    override fun onBackPressed() {
        stopService(Intent(this, FloatingWindow::class.java))
        finish()
        super.onBackPressed()
    }

    override fun onStart() {
        stopService(Intent(this, FloatingWindow::class.java))
        super.onStart()
    }
    override fun onResume() {
        stopService(Intent(this, FloatingWindow::class.java))
        super.onResume()
    }
    override fun onPause() {
        startService(Intent(this, FloatingWindow::class.java))
        super.onPause()
    }
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    fun addDataTofirebase(
        bugTitle: String,
        bugDes: String,
        deviceName: String,
        screenSize: String,
        appname: String,
        state: String
    ) {

        bugInfo = BugReport(
            bugTitle,
            bugDes,
            deviceName,
            screenSize,
            appname,
            state
        )
        databaseReference.push().setValue(bugInfo)
    }


}