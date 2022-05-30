package com.example.studentass.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.common.MemoryManager
import com.example.studentass.fragments.MainFragment.Companion.colorTheme
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Group
import com.example.studentass.models.User
import com.example.studentass.services.GroupApiService
import com.example.studentass.services.UserApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.account_data_item.view.*
import kotlinx.android.synthetic.main.color_theme_item.view.*
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    private val userApiService = UserApiService.create()
    private val groupApiService = GroupApiService.create()
    private val compositeDisposable = CompositeDisposable()
    private var colors: List<View>? = null
    private var colorsDrawable: List<Int>? = null
    val requestBody = "Bearer " + LoginFragment.token

    companion object {
        lateinit var currentUser: User
        lateinit var currentGroup: Group
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountLayout.background = colorTheme


        val drawable = DrawableCompat.wrap(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.account_data_background
            )
        }!!)
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
        val drawable1 = DrawableCompat.wrap(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.test_info_count
            )
        }!!)
        DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP)


        val disposableSubjectListRx = userApiService
            .getUser(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetUser(r) },
                { e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show() }
            )
        compositeDisposable.add(disposableSubjectListRx)


        colors = listOf<View>(
            color1,
            color2,
            color3,
            color4,
            color5,
            color6
        )
        colorsDrawable = listOf(
            R.drawable.colorback_white,
            R.drawable.colorback_pink,
            R.drawable.colorback_green,
            R.drawable.colorback_blue,
            R.drawable.ic_pattern_8,
            R.drawable.ic_pattern_5
        )
        for (x in 0..5) {
            val colorBack = DrawableCompat.wrap(context?.let {
                ContextCompat.getDrawable(
                    it,
                    colorsDrawable!![x]
                )
            }!!)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            colors!![x].view4.background = colorBack
            colors!![x].setOnFocusChangeListener { _, _ ->
                accountLayout.background = colorBack
                MemoryManager.deleteColorTheme(context!!)
                MemoryManager.saveColorTheme(x.toString(), context!!)
            }
        }



        onHiddenChanged(false)
    }

    private fun onGetUser(user: User?) {
        if (user != null) {
            currentUser = user

            val fio = "${currentUser.lastName} ${currentUser.firstName} ${currentUser.patronymic}"
            textView25.text = fio
            login.textView21.text = currentUser.login
            login.textView28.text = "Логин"
            email.textView21.text = currentUser.email
            email.textView28.text = getString(R.string.emailLabel)
            phone.textView21.text = currentUser.phone
            phone.textView28.text = "Телефон"
            getStudyGroup()
        }
    }

    private fun getStudyGroup() {

        val disposableSubjectListRx = groupApiService
            .getGroupById(requestBody, currentUser.studyGroupId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetGroup(r) },
                { e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show() }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun onGetGroup(r: Group) {
        currentGroup = r
        textView26.text = r.shortName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        getAppCompatActivity<MainActivity>()?.actionBar?.show()
        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Личный кабинет"
        }
    }
}