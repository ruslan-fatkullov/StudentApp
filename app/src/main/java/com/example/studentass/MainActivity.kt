package com.example.studentass

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.fragments.AboutProgramFragment
import com.example.studentass.fragments.LoginFragment
import com.example.studentass.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1


/*
 * Нужно для удобного получения экземпляра MainActivity из фрагментов
 */
@Suppress("UNCHECKED_CAST")
fun <T : AppCompatActivity> Fragment.getAppCompatActivity(): T? {
    return activity as T?
}


/*
 * Единственная активити в приложении
 */
class MainActivity : AppCompatActivity() {
    lateinit var fragmentManager: FragmentManager
    lateinit var actionBar: ActionBar

    private var fragmentLayersDepth = -1                                                    // Номер текущего слоя (фрагмента) в контейнере активити
    private val fragmentLayersMaxDepth = 32
    private val fragmentLayers = arrayOfNulls<Fragment?>(fragmentLayersMaxDepth)            // Список слоёв (фргментов) в контейнере активити
    private var fragmentsMainContainerId by Delegates.notNull<Int>()                        // ID контейнера активити

    /*
     * Создаёт панель действий
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }


    /*
     * Обрабатывает нажатия на кнопки панели действий
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ab_exit -> {
                switchSideways(LoginFragment::class.java)
                LoginFragment.logOut()
            }
            R.id.ab_about_program -> {
                switchUp(AboutProgramFragment::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /*
     * Обрабытывает нажатие на кнопку "назад"
     */
    override fun onBackPressed() {
        if (fragmentLayersDepth < 1) {
            finishAffinity()
        } else {
            switchDown()
        }
    }


    /*
     * Обрабытывает нажатие на кнопку "назад"
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        actionBar = (this as AppCompatActivity).supportActionBar
            ?: throw Exception("Action bar missing")
        actionBar.hide()

        fragmentsMainContainerId = main_activity_fragment_container.id

        LoginFragment.init(this)
        if (LoginFragment.token == null) {
            switchUp(LoginFragment::class.java)
        } else {
            switchUp(MainFragment::class.java)
        }
    }


    /*
     * Перемещение на слой (фрагментов) выше. Прячет текущий фрагмент, создавая новый
     */
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


    /*
     * Перемещение на слой (фрагментов) ниже. Удаляет текущий фрагмент, показывая старый
     */
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


    /*
     * Перемещение на слой (фрагментов) вбок. Удаляет текущий фрагмент и создаёт новый на том же слое
     */
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


    /*
     * Возврращение к тестам
     */
    fun backToTest() {
        if (fragmentLayersDepth < 1) {
            throw RuntimeException("SwitchDown error: fragment layers depth is below 1")
        }

        val currentFragment = fragmentLayers[fragmentLayersDepth]
            ?: throw RuntimeException("SwitchDown error: current fragment is null")
        fragmentManager.beginTransaction().remove(currentFragment).commit()
        fragmentLayers[fragmentLayersDepth] = null

        val newFragment = fragmentLayers[--fragmentLayersDepth]
            ?: throw RuntimeException("SwitchDown error: new fragment is null")
        ////
        fragmentManager.beginTransaction().remove(newFragment).commit()
        fragmentLayers[fragmentLayersDepth] = null
        ////

        fragmentManager.beginTransaction().add(fragmentsMainContainerId, newFragment).commit()
        fragmentLayers[++fragmentLayersDepth] = newFragment
    }



}