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

        fun sendGet(url : String) : String {
            val url = URL(url)
            var responceStr : String;
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                responceStr = inputStream.use { it.reader().use { reader -> reader.readText() } }
                //println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                /*inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        println(line)
                    }
                }*/
            }
            return responceStr;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //textViewMessage = findViewById(R.id.textViewMessage)
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
        //textViewMessage?.text = message
        /*thread {
            textViewMessage?.text = sendGet("https://my-json-server.typicode.com/fridayeveryday/testService/test")
            //textViewMessage?.text = sendGet("https://4b7af1df-c62e-49e5-b0a5-929837fb7e36.mock.pstmn.io/group/?name_group=testgrp")
        }*/
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