package com.example.studentass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.lang.Exception
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    companion object {
        var instance: MainActivity? = null

        val mHandler = Handler(Looper.getMainLooper())

        val client = OkHttpClient()

        const val rootUrl = "http://test.asus.russianitgroup.ru/api"
    }

    lateinit var sfm: FragmentManager

    lateinit var currentFragment: Fragment
    var fragmentsList = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (instance != null) {
            instance!!.finish()
            Toast.makeText(this, "Error: second MainActivity instance detected", Toast.LENGTH_LONG).show()
        }

        instance = this
        sfm = supportFragmentManager

        thread {
            LoginFragment.loadLoginData(this)
            try {
                LoginFragment.executeLogin()
                switchFragment(MainFragment::class.java)
            }
            catch (e: Exception) {
                switchFragment(LoginFragment::class.java)
                if (e !is LoginFragment.Companion.NoDataException) {
                    mHandler.post {
                        Toast.makeText(this, "Login error: $e (${e.message})", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun <T: Fragment> switchFragment(toEntityClass: Class<T>, removeOtherInstances: Boolean = true) {
        if (::currentFragment.isInitialized && currentFragment::javaClass == toEntityClass) {
            throw Exception("Can't re-instantiate fragment")
        }
        var fragmentAlreadyExists = false
        for (fr in fragmentsList) {
            if (fr::javaClass == toEntityClass) {
                fragmentAlreadyExists = true
                currentFragment = fr
                sfm.beginTransaction().show(currentFragment).commit()
            }
            else {
                if (removeOtherInstances) {
                    sfm.beginTransaction().remove(fr).commit()
                }
                else {
                    sfm.beginTransaction().hide(fr).commit()
                }
            }
        }
        if (!fragmentAlreadyExists) {
            currentFragment = toEntityClass.newInstance()
            fragmentsList.add(currentFragment)
            sfm.beginTransaction().add(main_activity_fragment_container.id, currentFragment).commit()
        }
    }
}