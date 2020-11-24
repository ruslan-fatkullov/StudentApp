package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentass.MainActivity
import com.example.studentass.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var currentFragment: Fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleFragment = ScheduleFragment()
        val subjectsFragment = SubjectsFragment()
        val notificationsFragment = NotificationsFragment()

        MainActivity.sfm.beginTransaction().add(fragment_container.id, scheduleFragment).commit()
        MainActivity.sfm.beginTransaction().hide(scheduleFragment).commit()

        MainActivity.sfm.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
        MainActivity.sfm.beginTransaction().hide(subjectsFragment).commit()

        MainActivity.sfm.beginTransaction().add(fragment_container.id, notificationsFragment).commit()
        MainActivity.sfm.beginTransaction().hide(notificationsFragment).commit()

        currentFragment = scheduleFragment

        bottomNavigationView.setOnNavigationItemSelectedListener {
            MainActivity.sfm.beginTransaction().hide(currentFragment).commit()
            currentFragment = when (it.itemId) {
                R.id.bnv_schedule -> scheduleFragment
                R.id.bnv_subjects -> subjectsFragment
                R.id.bnv_notifications -> notificationsFragment
                else -> currentFragment
            }
            MainActivity.sfm.beginTransaction().show(currentFragment).commit()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (::currentFragment.isInitialized) {
            if (hidden) {
                MainActivity.sfm.beginTransaction().hide(currentFragment).commit()
            }
            else {
                MainActivity.sfm.beginTransaction().show(currentFragment).commit()
            }
        }
    }
}