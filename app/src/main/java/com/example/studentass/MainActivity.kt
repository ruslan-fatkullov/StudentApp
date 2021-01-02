package com.example.studentass

import android.content.Intent
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
import kotlin.concurrent.thread
import kotlin.properties.Delegates


@Suppress("UNCHECKED_CAST")
fun <T : AppCompatActivity> Fragment.getAppCompatActivity(): T? {
    return activity as T?
}

class MainActivity : AppCompatActivity() {
    lateinit var fragmentManager: FragmentManager
    lateinit var actionBar: ActionBar

    private var fragmentLayersDepth = -1
    private val fragmentLayersMaxDepth = 32
    private val fragmentLayers = arrayOfNulls<Fragment?>(fragmentLayersMaxDepth)
    private var fragmentsMainContainerId by Delegates.notNull<Int>()

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
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Logout error: $e (${e.message})",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    switchSideways(LoginFragment::class.java)
                    LoginFragment.deleteLoginData(this)
                }
            }
            R.id.ab_about_program -> {
                switchUp(AboutProgramFragment::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (fragmentLayersDepth < 1) {
            finishAffinity()
        } else {
            switchDown()
        }
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

        fragmentsMainContainerId = main_activity_fragment_container.id

        LoginFragment.init(this)

        thread {
            LoginFragment.loadLoginData(this)
            try {
                if (LoginFragment.loginTokens == null) {
                    LoginFragment.executeLogin()
                }
                switchUp(MainFragment::class.java)
            }
            catch (e: Exception) {
                switchUp(LoginFragment::class.java)
                if (e !is LoginFragment.Companion.NoDataException) {
                    runOnUiThread {
                        Toast.makeText(this, "Login error: $e (${e.message})", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun <T : Fragment> switchUp(toEntityClass: Class<T>) {
        if (fragmentLayersDepth < -1) {
            throw RuntimeException("SwitchUp error: fragment layers depth is below -1")
        }

        if (fragmentLayersDepth >= 0) {
            val currentFragment = fragmentLayers[fragmentLayersDepth]
                ?: throw RuntimeException("SwitchUp error: current fragment is null")
            fragmentManager.beginTransaction().hide(currentFragment).commit()
        }

        val newFragment = toEntityClass.newInstance()
        fragmentManager.beginTransaction().add(fragmentsMainContainerId, newFragment).commit()
        fragmentLayers[++fragmentLayersDepth] = newFragment
    }

    fun switchDown() {
        if (fragmentLayersDepth < 1) {
            throw RuntimeException("SwitchDown error: fragment layers depth is below 1")
        }

        val currentFragment = fragmentLayers[fragmentLayersDepth]
            ?: throw RuntimeException("SwitchDown error: current fragment is null")
        fragmentManager.beginTransaction().remove(currentFragment).commit()
        fragmentLayers[fragmentLayersDepth] = null

        val newFragment = fragmentLayers[--fragmentLayersDepth]
            ?: throw RuntimeException("SwitchDown error: new fragment is null")
        fragmentManager.beginTransaction().show(newFragment).commit()
    }

    fun <T : Fragment> switchSideways(toEntityClass: Class<T>) {
        if (fragmentLayersDepth < 0) {
            throw RuntimeException("SwitchSideways error: fragment layers depth is below 0")
        }

        val currentFragment = fragmentLayers[fragmentLayersDepth]
            ?: throw RuntimeException("SwitchSideways error: current fragment is null")
        fragmentManager.beginTransaction().remove(currentFragment).commit()

        val newFragment = toEntityClass.newInstance()
        fragmentManager.beginTransaction().add(fragmentsMainContainerId, newFragment).commit()
        fragmentLayers[fragmentLayersDepth] = newFragment
    }
}