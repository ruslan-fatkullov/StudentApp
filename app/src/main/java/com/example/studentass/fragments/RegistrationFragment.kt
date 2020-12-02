package com.example.studentass.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.studentass.MainActivity.Companion.mainActivity
import com.example.studentass.R
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    var nameValidity: String? = null
    var groupValidity: String? = null
    var emailRegValidity: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameValidity = validateName(s.toString())
                if (nameValidity == "") {
                    nameIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    nameOkIv.visibility = View.VISIBLE
                }
                else {
                    nameIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    nameOkIv.visibility = View.GONE
                }
            }
        })
        groupEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                groupValidity = validateGroup(s.toString())
                if (groupValidity == "") {
                    groupIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    groupOkIv.visibility = View.VISIBLE
                }
                else {
                    groupIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    groupOkIv.visibility = View.GONE
                }
            }
        })
        emailRegEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailRegValidity = validateEmailReg(s.toString())
                if (emailRegValidity == "") {
                    emailRegIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    emailRegOkIv.visibility = View.VISIBLE
                }
                else {
                    emailRegIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    emailRegOkIv.visibility = View.GONE
                }
            }
        })
        signupBn.setOnClickListener { onSignupButtonClick() }
        loginTv.setOnClickListener { onLoginTextViewClick() }
        onHiddenChanged(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mainActivity.sab.hide()
        }
    }

    private fun validateName(name: String): String {
        if (name.isEmpty()) return "Поле не должно быть пустым"
        return ""
    }

    private fun validateGroup(group: String): String {
        if (group.isEmpty()) return "Поле не должно быть пустым"
        return ""
    }

    private fun validateEmailReg(emailReg: String): String {
        if (emailReg.isEmpty()) return "Поле не должно быть пустым"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailReg).matches()) return "Неверый формат"
        return ""
    }

    private fun onSignupButtonClick() {
        if (nameValidity == null) nameValidity = validateName(nameEt.text.toString())
        if (groupValidity == null) groupValidity = validateGroup(groupEt.text.toString())
        if (emailRegValidity == null) emailRegValidity = validateEmailReg(emailRegEt.text.toString())
        var validData = true

        if (nameValidity != "") {
            nameEt.error = nameValidity
            validData = false
        }
        if (groupValidity != "") {
            groupEt.error = groupValidity
            validData = false
        }
        if (emailRegValidity != "") {
            emailRegEt.error = emailRegValidity
            validData = false
        }

        if (validData) {
            signupBn.startAnimation()
            //login(emailText, passwordText)
            //loginRole = "student"
            //MainActivity.switchFragment(this, MainActivity.mainFragment)
            mainActivity.switchFragment(LoginFragment::class.java)
        }
        else {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            signupBn.startAnimation(shake)
            Toast.makeText(context, "Обнаружены некорректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLoginTextViewClick() {
        mainActivity.switchFragment(LoginFragment::class.java)
    }
}