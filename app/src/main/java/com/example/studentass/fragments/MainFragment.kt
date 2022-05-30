package com.example.studentass.fragments

import android.graphics.drawable.Drawable
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


/*
 * Управляет основными страницами, доступными студенту
 */
class MainFragment : Fragment() {
    private lateinit var sfm: FragmentManager
    private lateinit var currentFragment: Fragment

    companion object {
        var colorTheme: Drawable? = null
    }


    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorTheme = context?.let {
            getAppCompatActivity<MainActivity>()?.getColorTheme(it)
        }

        sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager

        val ratingFragment = RatingFragment()
        val scheduleFragmentNew = ScheduleFragmentNew()
        val subjectsFragmentNew = SubjectsFragmentNew()

        val notificationsFragment = NotificationsFragment()
        val accountFragment = AccountFragment()

        sfm.beginTransaction().add(fragment_container.id, scheduleFragmentNew).commit()
        sfm.beginTransaction().hide(scheduleFragmentNew).commit()

        sfm.beginTransaction().add(fragment_container.id, subjectsFragmentNew).commit()
        sfm.beginTransaction().hide(subjectsFragmentNew).commit()

        sfm.beginTransaction().add(fragment_container.id, ratingFragment).commit()
        sfm.beginTransaction().hide(ratingFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, notificationsFragment).commit()
        sfm.beginTransaction().hide(notificationsFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, accountFragment).commit()
        sfm.beginTransaction().hide(accountFragment).commit()





        currentFragment = scheduleFragmentNew
        sfm.beginTransaction().show(currentFragment).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            sfm.beginTransaction().hide(currentFragment).commit()
            currentFragment = when (it.itemId) {
                R.id.bnv_schedule -> scheduleFragmentNew
                R.id.bnv_subjects -> subjectsFragmentNew
                R.id.bnv_rating -> ratingFragment
                R.id.bnv_account -> accountFragment
                //R.id.bnv_account -> notificationsFragment
                else -> currentFragment
            }
            sfm.beginTransaction().show(currentFragment).commit()
            true
        }




        onHiddenChanged(false)
    }


    /*
     * Наполнение фрагмента интерфейсом
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    /*
     * Управляет видамостию панели дайствий
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.show()
        }

        if (::currentFragment.isInitialized) {
            if (hidden) {
                sfm.beginTransaction().hide(currentFragment).commit()
            } else {
                sfm.beginTransaction().show(currentFragment).commit()
            }
        }
    }
}