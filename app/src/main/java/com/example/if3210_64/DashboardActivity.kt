package com.example.if3210_64

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.if3210_64.fragments.NewsFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val newsFragment = NewsFragment()
    // private val locationFragment = locationFragment()
    // private val bookmarkFragment = bookmarkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        replaceFragment(newsFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_news -> replaceFragment(newsFragment)
                //R.id.ic_location -> replaceFragment(locationFragment)
                //R.id.ic_bookmark -> replaceFragment(bookmarkFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}