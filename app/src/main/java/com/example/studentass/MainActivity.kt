package com.example.studentass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    companion object {
        private var ma: MainActivity? = null
        var mainActivity: MainActivity
            get() = if (ma != null) {ma!!} else {throw Exception("Error: no instance of main activity")}
            set(_) {}

        val mHandler = Handler(Looper.getMainLooper())

        val client = OkHttpClient()

        const val rootUrl = "http://test.asus.russianitgroup.ru/api"
    }

    lateinit var sfm: FragmentManager
    lateinit var sab: ActionBar

    lateinit var currentFragment: Fragment
    var fragmentsList = mutableListOf<Fragment>()

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ab_exit -> {
                thread {
                    try {
                        LoginFragment.executeLogout()
                    }
                    catch (e: Exception) {
                        mHandler.post {
                            Toast.makeText(mainActivity, "Logout error: $e (${e.message})", Toast.LENGTH_LONG).show()
                        }
                    }
                    mainActivity.switchFragment(LoginFragment::class.java)
                    LoginFragment.deleteLoginData(mainActivity)
                }
            }
            R.id.ab_about_program -> {
                
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ma = if (ma == null) {this} else {
            Toast.makeText(this, "Error: second MainActivity instance detected", Toast.LENGTH_LONG).show()
            throw Exception("Error: second MainActivity instance detected")
        }

        sfm = supportFragmentManager
        val qsab = (this as AppCompatActivity).supportActionBar
        if (qsab == null) {
            throw Exception("Action bar missing")
        }
        else {
            sab = qsab
        }
        sab.hide()

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

    fun <T : Fragment> switchFragment(toEntityClass: Class<T>, removeOtherInstances: Boolean = true) {
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