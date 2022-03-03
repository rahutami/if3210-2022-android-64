package com.example.if3210_64

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.if3210_64.fragments.ListBookmarkFragment
import com.example.if3210_64.fragments.ListFaskesFragment
import com.example.if3210_64.fragments.NewsFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val newsFragment = NewsFragment()
    private val listFaskesFragment = ListFaskesFragment()
    private val listBookmarkFragment = ListBookmarkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        replaceFragment(newsFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_news -> replaceFragment(newsFragment)
                R.id.ic_location -> replaceFragment(listFaskesFragment)
                R.id.ic_bookmark -> replaceFragment(listBookmarkFragment)
                R.id.ic_qr_code -> changeActivitytoCheckIn()
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

    private fun changeActivitytoCheckIn() {
        val intent = Intent(this, CheckIn::class.java)
        startActivity(intent)
    }
}