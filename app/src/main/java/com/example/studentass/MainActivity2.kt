package com.example.studentass

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.studentass.fragments.NotificationsFragment
import com.example.studentass.fragments.ScheduleFragment
import com.example.studentass.fragments.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main2.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {
    var textViewMessage : TextView? = null
    var bnv : BottomNavigationView? = null

    companion object {
        const val MESSAGE = "message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        bnv = findViewById(R.id.bottomNavigationView)

        val scheduleFragment = ScheduleFragment()
        val subjectsFragment = SubjectsFragment()
        val notificationsFragment = NotificationsFragment()

        makeCurrentFragment(scheduleFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_schedule -> makeCurrentFragment(scheduleFragment)
                R.id.bnv_subjects -> makeCurrentFragment(subjectsFragment)
                R.id.bnv_notifications -> makeCurrentFragment(notificationsFragment)
            }
            true
        }


        val message = intent.getStringExtra(MESSAGE)
    }

    fun onButtonBackClick(view: View) {
        val intentActivity = Intent(this, MainActivity::class.java)
        startActivity(intentActivity)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}