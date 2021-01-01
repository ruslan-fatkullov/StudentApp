package com.example.studentass

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.AboutProgramFragment
import com.example.studentass.fragments.LoginFragment
import com.example.studentass.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import kotlin.concurrent.thread

@Suppress("UNCHECKED_CAST")
fun <T: AppCompatActivity> Fragment.getAppCompatActivity(): T? {
    return activity as T?
}

class MainActivity : AppCompatActivity() {
    companion object {
        const val rootUrl = "http://test.asus.russianitgroup.ru/api"
    }

    lateinit var fragmentManager: FragmentManager
    lateinit var actionBar: ActionBar

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
                        runOnUiThread {
                            Toast.makeText(this, "Logout error: $e (${e.message})", Toast.LENGTH_LONG).show()
                        }
                    }
                    this.switchFragment(LoginFragment::class.java)
                    LoginFragment.deleteLoginData(this)
                }
            }
            R.id.ab_about_program -> {
                switchFragment(AboutProgramFragment::class.java, false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        val qsab = (this as AppCompatActivity).supportActionBar
        if (qsab == null) {
            throw Exception("Action bar missing")
        }
        else {
            actionBar = qsab
        }
        actionBar.hide()

        thread {
            LoginFragment.loadLoginData(this)
            try {
                if (LoginFragment.loginTokens == null) {
                    LoginFragment.executeLogin()
                }
                switchFragment(MainFragment::class.java)
            }
            catch (e: Exception) {
                switchFragment(LoginFragment::class.java)
                if (e !is LoginFragment.Companion.NoDataException) {
                    runOnUiThread {
                        Toast.makeText(this, "Login error: $e (${e.message})", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun <T : Fragment> switchFragment(toEntityClass: Class<T>, removeOtherInstances: Boolean = true) {
        if (::currentFragment.isInitialized && currentFragment.javaClass == toEntityClass) {
            throw Exception("Can't re-instantiate fragment")
        }
        var fragmentAlreadyExists = false
        for (fr in fragmentsList) {
            if (fr.javaClass == toEntityClass) {
                fragmentAlreadyExists = true
                currentFragment = fr
                showFragment(currentFragment)
            }
            else {
                if (removeOtherInstances) {
                    fragmentsList.remove(fr)
                    removeFragment(fr)
                }
                else {
                    hideFragment(fr)
                }
            }
        }
        if (!fragmentAlreadyExists) {
            currentFragment = toEntityClass.newInstance()
            fragmentsList.add(currentFragment)
            addFragment(currentFragment)
        }
    }

    fun addFragmentTo(fr: Fragment, containerId: Int) {
        fragmentManager.beginTransaction().add(containerId, fr).commit()
    }

    fun addFragment(fr: Fragment) {
        addFragmentTo(fr, main_activity_fragment_container.id)
    }

    fun removeFragment(fr: Fragment) {
        fragmentManager.beginTransaction().remove(fr).commit()
    }

    fun showFragment(fr: Fragment) {
        fragmentManager.beginTransaction().show(fr).commit()
    }

    fun hideFragment(fr: Fragment) {
        fragmentManager.beginTransaction().hide(fr).commit()
    }
}