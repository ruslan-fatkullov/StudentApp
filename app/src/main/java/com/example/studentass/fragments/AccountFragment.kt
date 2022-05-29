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
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Group
import com.example.studentass.models.User
import com.example.studentass.services.GroupApiService
import com.example.studentass.services.UserApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    private val userApiService = UserApiService.create()
    private val groupApiService = GroupApiService.create()
    private val compositeDisposable = CompositeDisposable()
    val requestBody = "Bearer " + LoginFragment.token
    companion object{
        lateinit var currentUser: User
        lateinit var currentGroup: Group
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var drawable = DrawableCompat.wrap(context?.let { ContextCompat.getDrawable(it, R.drawable.account_data_background) }!!)
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
        var drawable1 = DrawableCompat.wrap(context?.let { ContextCompat.getDrawable(it, R.drawable.test_info_count) }!!)
        DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP)
        var drawable2 = DrawableCompat.wrap(context?.let { ContextCompat.getDrawable(it, R.drawable.change_pass_button) }!!)
        DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP)

        firstnameOfUser.background = drawable
        lastnameOfUser.background = drawable
        patronomycOfUser.background = drawable
        emailOfUser.background = drawable
        loginOfUser.background = drawable
        phoneOfUser.background = drawable
        groupOfUser.background = drawable
        privateDataCL.background = drawable1
        accountDataCL.background = drawable1

        changePassButton.background = drawable2

        val disposableSubjectListRx = userApiService
            .getUser(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetUser(r)},
                {e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)



        onHiddenChanged(false)
    }

    private fun onGetUser(user: User?) {
        if (user != null) {
            currentUser = user


            firstNameLabelTV.text = currentUser.firstName
            lastnameOfUserTV.text = currentUser.lastName
            patronomycTV.text = currentUser.patronymic
            emailTV.text = currentUser.email
            loginTV.text = currentUser.login
            phoneTV.text = currentUser.phone


            getStudyGroup()
        }
    }

    private  fun getStudyGroup(){

        val disposableSubjectListRx = groupApiService
            .getGroupById(requestBody, currentUser.studyGroupId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetGroup(r)},
                {e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun onGetGroup(r: Group) {
        currentGroup = r
        groupTV.text = r.shortName
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