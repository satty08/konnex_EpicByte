package com.konex.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.konex.R
import kotlinx.android.synthetic.main.activity_floting_permission.*


class FlotingPermission : AppCompatActivity() {
    private val SYSTEM_ALERT_WINDOW_PERMISSION = 2084
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floting_permission)
        allow_btn.setOnClickListener {
            if (!Settings.canDrawOverlays(this)){
                askPermission()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

}