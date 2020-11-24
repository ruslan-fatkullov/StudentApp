package com.example.studentass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        val mHandler = Handler(Looper.getMainLooper())

        const val rootUrl = "http://test.asus.russianitgroup.ru/api"

        lateinit var sfm: FragmentManager

        val loginFragment = LoginFragment()
        val registrationFragment = RegistrationFragment()
        val mainFragment = MainFragment()

        fun switchFragment(from: LoginFragment, to: Fragment) {
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