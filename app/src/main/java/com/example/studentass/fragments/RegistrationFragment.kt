package com.example.studentass.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.studentass.MainActivity
import com.example.studentass.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    var nameIsValid = false
    var groupIsValid = false
    var emailRegIsValid = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidName(s.toString())) {
                    nameIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    nameOkIv.visibility = View.VISIBLE
                    nameIsValid = true
                }
                else {
                    nameIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    nameOkIv.visibility = View.GONE
                    nameIsValid = false
                }
            }
        })
        groupEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidGroup(s.toString())) {
                    groupIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    groupOkIv.visibility = View.VISIBLE
                    groupIsValid = true
                }
                else {
                    groupIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    groupOkIv.visibility = View.GONE
                    groupIsValid = false
                }
            }
        })
        emailRegEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidEmail(s.toString())) {
                    emailRegIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    emailRegOkIv.visibility = View.VISIBLE
                    emailRegIsValid = true
                }
                else {
                    emailRegIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    emailRegOkIv.visibility = View.GONE
                    emailRegIsValid = false
                }
            }
        })
        loginTv.setOnClickListener { _ -> onLoginTextViewClick() }
    }

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isValidGroup(group: String): Boolean {
        return group.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun onLoginTextViewClick() {
        MainActivity.switchFragment(this, MainActivity.loginFragment)
    }
}