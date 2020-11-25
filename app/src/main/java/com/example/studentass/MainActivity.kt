package com.example.studentass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    var loginFragment: LoginFragment? = null
    var registrationFragment: RegistrationFragment? = null
    var mainFragment: MainFragment? = null

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
                goToMain()
            }
            catch (e: Exception) {
                goToLogin()
                if (e !is LoginFragment.Companion.NoDataException) {
                    mHandler.post {
                        Toast.makeText(this, "Login error: $e (${e.message})", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun goToMain() {
        if (mainFragment == null) {
            mainFragment = MainFragment()
            sfm.beginTransaction().add(main_activity_fragment_container.id, mainFragment!!).commit()
        }
        else {
            sfm.beginTransaction().show(mainFragment!!).commit()
        }
        if (loginFragment != null) {
            sfm.beginTransaction().remove(loginFragment!!).commit()
            loginFragment = null
        }
        if (registrationFragment != null) {
            sfm.beginTransaction().remove(registrationFragment!!).commit()
            registrationFragment = null
        }
    }

    fun goToRegistration() {
        if (registrationFragment == null) {
            registrationFragment = RegistrationFragment()
            sfm.beginTransaction().add(main_activity_fragment_container.id, registrationFragment!!).commit()
        }
        else {
            sfm.beginTransaction().show(registrationFragment!!).commit()
        }
        if (loginFragment != null) {
            sfm.beginTransaction().hide(loginFragment!!).commit()
        }
        if (mainFragment != null) {
            sfm.beginTransaction().remove(mainFragment!!).commit()
            mainFragment = null
        }
    }

    fun goToLogin() {
        if (loginFragment == null) {
            loginFragment = LoginFragment()
            sfm.beginTransaction().add(main_activity_fragment_container.id, loginFragment!!).commit()
        }
        else {
            sfm.beginTransaction().show(loginFragment!!).commit()
        }
        if (registrationFragment != null) {
            sfm.beginTransaction().remove(registrationFragment!!).commit()
            registrationFragment = null
        }
        if (mainFragment != null) {
            sfm.beginTransaction().remove(mainFragment!!).commit()
            mainFragment = null
        }
    }
}