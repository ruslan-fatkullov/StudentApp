package com.example.studentass

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.text.CaseMap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        val mHandler = Handler(Looper.getMainLooper())

        lateinit var sfm: FragmentManager

        val loginFragment = LoginFragment()
        val registrationFragment = RegistrationFragment()
        val mainFragment = MainFragment()

        fun switchFragment(from: Fragment, to: Fragment) {
            sfm.beginTransaction().hide(from).commit()
            sfm.beginTransaction().show(to).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sfm = supportFragmentManager

        sfm.beginTransaction().add(main_fragment_container.id, loginFragment).commit()

        sfm.beginTransaction().add(main_fragment_container.id, registrationFragment).commit()
        sfm.beginTransaction().hide(registrationFragment).commit()

        sfm.beginTransaction().add(main_fragment_container.id, mainFragment).commit()
        sfm.beginTransaction().hide(mainFragment).commit()
    }
}