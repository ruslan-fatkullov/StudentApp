package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.studentass.MainActivity
import com.example.studentass.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thisActivity = activity!!


        if ("student" == "student") {
            val scheduleFragment = ScheduleFragment()
            val subjectsFragment = SubjectsFragment()
            val notificationsFragment = NotificationsFragment()

            thisActivity.supportFragmentManager.beginTransaction().add(fragment_container.id, scheduleFragment).commit()
            thisActivity.supportFragmentManager.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
            thisActivity.supportFragmentManager.beginTransaction().add(fragment_container.id, notificationsFragment).commit()

            thisActivity.title = "Расписание занятий"
            thisActivity.supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
            thisActivity.supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()

            bottomNavigationView.setOnNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.bnv_schedule -> {
                        //makeCurrentFragment(scheduleFragment)
                        thisActivity.supportFragmentManager.beginTransaction().show(scheduleFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                        thisActivity.title = "Расписание занятий"

                    }
                    R.id.bnv_subjects -> {
                        //makeCurrentFragment(subjectsFragment)
                        thisActivity.supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().show(subjectsFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().hide(notificationsFragment).commit()
                        thisActivity.title = "Предметы"
                    }
                    R.id.bnv_notifications -> {
                        //makeCurrentFragment(notificationsFragment)
                        thisActivity.supportFragmentManager.beginTransaction().hide(scheduleFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().hide(subjectsFragment).commit()
                        thisActivity.supportFragmentManager.beginTransaction().show(notificationsFragment).commit()
                        thisActivity.title = "Уведомления"
                    }
                }
                true
            }
        }
        else {
            Toast.makeText(context, "This app is only for students, and you're ${555}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}