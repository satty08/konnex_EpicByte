package com.konex.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.konex.R
import com.konex.extra.BugListAdapter
import com.konex.extra.FloatingWindow
import com.konex.model.BugListItem
import kotlinx.android.synthetic.main.activity_bug_list.*

class BugList : AppCompatActivity() {
    private lateinit var fDatabase: DatabaseReference
    lateinit var bugData:MutableList<BugListItem>
    lateinit var bugAdapter:BugListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_list)
        bug_list_back.setOnClickListener {
            onBackPressed()
        }
        fDatabase = FirebaseDatabase.getInstance().reference
        bugData = ArrayList()
        fDatabase.child("Bugs").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progess.visibility = View.GONE
                bugData.add(BugListItem(snapshot.child("appName").value.toString(),
                    snapshot.child("bugDes").value.toString(),
                    snapshot.child("bugTitle").value.toString(),
                    snapshot.child("deviceName").value.toString(),
                    snapshot.child("screenSize").value.toString(),
                    snapshot.child("status").value.toString()))
                rv_bug_list.layoutManager = LinearLayoutManager(this@BugList,LinearLayoutManager.VERTICAL,false)
                bugAdapter = BugListAdapter(this@BugList,bugData)
                rv_bug_list.adapter = bugAdapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })



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