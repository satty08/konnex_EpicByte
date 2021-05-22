package com.konex.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.konex.R
import com.konex.extra.FloatingWindow
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        admin_back.setOnClickListener {
            onBackPressed()
        }
        sign_in_btn.setOnClickListener {
            if (TextUtils.isEmpty(user_id.text.toString())){
                user_id.error = "Enter User id"
            }else if(TextUtils.isEmpty(password.text.toString())){
                password.error = "Enter password"
            }else{
                if (user_id.text.toString() == "konex@gmail.com" && password.text.toString() == "12345"){
                    startActivity(Intent(this,BugList::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "User id or password not matched", Toast.LENGTH_SHORT).show()
                }
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

    override fun onDestroy() {
        stopService(Intent(this, FloatingWindow::class.java))
        super.onDestroy()
    }
}