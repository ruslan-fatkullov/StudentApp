package com.example.studentass

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentass.fragments.NotificationsFragment
import com.example.studentass.fragments.ScheduleFragment
import com.example.studentass.fragments.SubjectsFragment
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (AuthActivity.loginRole == "student") {
            val scheduleFragment = ScheduleFragment()
            val subjectsFragment = SubjectsFragment()
            val notificationsFragment = NotificationsFragment()

            supportFragmentManager.beginTransaction().add(fragment_container.id, scheduleFragment).commit()
            supportFragmentManager.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
            supportFragmentManager.beginTransaction().add(fragment_container.id, notificationsFragment).commit()

            title = "Расписание"
            supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
            supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()

            bottomNavigationView.setOnNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.bnv_schedule -> {
                        //makeCurrentFragment(scheduleFragment)
                        supportFragmentManager.beginTransaction().show(scheduleFragment).commit()
                        supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                        supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                        title = "Расписание"
                    }
                    R.id.bnv_subjects -> {
                        //makeCurrentFragment(subjectsFragment)
                        supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                        supportFragmentManager.beginTransaction().show(subjectsFragment).commit()
                        supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                        title = "Предметы"
                    }
                    R.id.bnv_notifications -> {
                        //makeCurrentFragment(notificationsFragment)
                        supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                        supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                        supportFragmentManager.beginTransaction().show(notificationsFragment).commit()
                        title = "Уведомления"
                    }
                }
                true
            }
        }
        else {
            Toast.makeText(applicationContext, "This app is only for students, and you're ${AuthActivity.loginRole}", Toast.LENGTH_LONG).show()
        }
    }
}