package com.example.studentass

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.studentass.fragments.NotificationsFragment
import com.example.studentass.fragments.ScheduleFragment
import com.example.studentass.fragments.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {
    var bnv : BottomNavigationView? = null
    val scheduleFragment : ScheduleFragment? = null
    val subjectsFragment : ScheduleFragment? = null
    val notificationsFragment : ScheduleFragment? = null

    companion object {
        const val MESSAGE = "message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val scheduleFragment = ScheduleFragment()
        val subjectsFragment = SubjectsFragment()
        val notificationsFragment = NotificationsFragment()

        supportFragmentManager.beginTransaction().add(fragment_container.id, scheduleFragment).commit()
        supportFragmentManager.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
        supportFragmentManager.beginTransaction().add(fragment_container.id, notificationsFragment).commit()

        supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
        supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.bnv_schedule -> {
                    //makeCurrentFragment(scheduleFragment)
                    supportFragmentManager.beginTransaction().show(scheduleFragment).commit()
                    supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                    supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                }
                R.id.bnv_subjects -> {
                    //makeCurrentFragment(subjectsFragment)
                    supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                    supportFragmentManager.beginTransaction().show(subjectsFragment).commit()
                    supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                }
                R.id.bnv_notifications -> {
                    //makeCurrentFragment(notificationsFragment)
                    supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                    supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                    supportFragmentManager.beginTransaction().show(notificationsFragment).commit()
                }
            }
            true
        }

        //val message = intent.getStringExtra(MESSAGE)
    }

    fun onButtonBackClick(view: View) {
        val intentActivity = Intent(this, AuthActivity::class.java)
        startActivity(intentActivity)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}