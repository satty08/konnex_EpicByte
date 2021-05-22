package com.konex.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.konex.R
import com.konex.extra.FloatingWindow
import com.konex.extra.HideSearchBar
import com.konex.extra.SearchBarInterface
import com.konex.fragment.ChatBotFragment
import com.konex.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SearchBarInterface {
    var isSearchFocusable = false
    var callInterface: HideSearchBar?=null
    var bugWindowOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(navListener)
        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.menu_home
        }
        bottom_navigation.itemIconTintList = null
    }

    override fun onPause() {
        if (!bugWindowOpen){
            startService(Intent(this, FloatingWindow::class.java))
        }

        super.onPause()
    }

    override fun onRestart() {
        stopService(Intent(this, FloatingWindow::class.java))
        super.onRestart()
    }

    override fun onResume() {
        bugWindowOpen = false
        stopService(Intent(this, FloatingWindow::class.java))
        super.onResume()
    }
    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            //drawerLayout.isEnabled=false
            when (item.itemId) {
                R.id.menu_home ->{
                    supportFragmentManager.popBackStack()
                    selectedFragment = HomeFragment()
                }

                R.id.menu_chat_bot ->{
                    supportFragmentManager.popBackStack()
                    selectedFragment = ChatBotFragment()
                }
            }
            // It will help to replace the one fragment to other.
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }

    override fun onBackPressed() {
        when{
            isSearchFocusable->{
                callInterface!!.hideSearchBar()
            }
            bottom_navigation.selectedItemId!= R.id.menu_home->{
                supportFragmentManager.popBackStack()
                bottom_navigation.selectedItemId = R.id.menu_home
                return
            }else->{
                super.onBackPressed()
            }
        }

    }

    override fun isSearchFocusable(focused: Boolean) {
        isSearchFocusable = focused
    }

    override fun bugReportWindow(click: Boolean) {
        bugWindowOpen = click
    }

    fun searchBar(hide:HideSearchBar){
        callInterface = hide
    }
}