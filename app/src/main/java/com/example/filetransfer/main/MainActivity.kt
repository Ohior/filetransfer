package com.example.filetransfer.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.filetransfer.R
import com.example.filetransfer.tools.Tools
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var idNavView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Call findViewById on the NavigationView
        idNavView = findViewById(R.id.id_navView)
        drawerManager()
        drawerMenuItems()
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun drawerMenuItems() {
        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        idNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.id_menu_drawer_picture -> {
                    Tools.goToFragment(supportFragmentManager, R.id.id_fragmentContainerView, ImagesFragment())
                    true
                }
                R.id.id_menu_drawer_apk -> {
                    Tools.goToFragment(supportFragmentManager, R.id.id_fragmentContainerView, ApkFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun drawerManager() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.id_drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        actionBarDrawerToggle.syncState()
    }

}