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
import com.example.studentass.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        val mHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().add(main_fragment_container.id, loginFragment).commit()
    }

    fun goToMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commit()
        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().add(main_fragment_container.id, mainFragment).commit()
    }
}