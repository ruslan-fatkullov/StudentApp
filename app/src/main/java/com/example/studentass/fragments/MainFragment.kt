package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var sfm: FragmentManager
    private lateinit var currentFragment: Fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager

        val ratingFragment = RatingFragment()
        val scheduleFragment = ScheduleFragment()
        val subjectsFragment = SubjectsFragment()
        val notificationsFragment = NotificationsFragment()

        sfm.beginTransaction().add(fragment_container.id, scheduleFragment).commit()
        sfm.beginTransaction().hide(scheduleFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
        sfm.beginTransaction().hide(subjectsFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, ratingFragment).commit()
        sfm.beginTransaction().hide(ratingFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, notificationsFragment).commit()
        sfm.beginTransaction().hide(notificationsFragment).commit()

        currentFragment = scheduleFragment
        sfm.beginTransaction().show(currentFragment).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            sfm.beginTransaction().hide(currentFragment).commit()
            currentFragment = when (it.itemId) {
                R.id.bnv_schedule -> scheduleFragment
                R.id.bnv_subjects -> subjectsFragment
                R.id.bnv_rating -> ratingFragment
                R.id.bnv_notifications -> notificationsFragment
                else -> currentFragment
            }
            sfm.beginTransaction().show(currentFragment).commit()
            true
        }

        /*headerExitBn.setOnClickListener {
            thread {
                try {
                    LoginFragment.executeLogout()
                }
                catch (e: Exception) {
                    MainActivity.mHandler.post {
                        Toast.makeText(MainActivity.mainActivity!!, "Logout error: $e (${e.message})", Toast.LENGTH_LONG).show()
                    }
                }
                MainActivity.mainActivity!!.switchFragment(LoginFragment::class.java)
                LoginFragment.deleteLoginData(MainActivity.mainActivity!!)
            }
        }*/
        onHiddenChanged(false)
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

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.show()
        }

        if (::currentFragment.isInitialized) {
            if (hidden) {
                sfm.beginTransaction().hide(currentFragment).commit()
            }
            else {
                sfm.beginTransaction().show(currentFragment).commit()
            }
        }
    }
}