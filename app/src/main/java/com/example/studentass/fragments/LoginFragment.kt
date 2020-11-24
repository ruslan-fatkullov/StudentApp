package com.example.studentass.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.auth0.android.jwt.JWT
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.example.studentass.models.AuthRefreshData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginFragment : Fragment() {
    private val credentialsLogin = "ritg"
    private val credentialsPassword = "ritg"

    var loginEmail: String? = null
    var loginPassword: String? = null

    var loginTokens = AuthLoginTokens("", "")
    var loginRole = "NONE"

    var emailValidity: String? = null
    var passwordValidity: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailValidity = validateEmail(s.toString())
                if (emailValidity == "") {
                    emailIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    emailOkIv.visibility = View.VISIBLE
                }
                else {
                    emailIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    emailOkIv.visibility = View.GONE
                }
            }
        })
        passwordEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordValidity = validatePassword(s.toString())
                if (passwordValidity == "") {
                    passwordIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    passwordOkIv.visibility = View.VISIBLE
                }
                else {
                    passwordIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    passwordOkIv.visibility = View.GONE
                }
            }
        })
        passwordModeIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
        passwordModeIv.setOnClickListener {
            if (passwordEt.transformationMethod == null) {
                passwordEt.transformationMethod = PasswordTransformationMethod()
                passwordModeIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
            }
            else {
                passwordEt.transformationMethod = null
                passwordModeIv.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
            }
        }
        loginBn.setOnClickListener { onLoginButtonClick() }
        registrationTv.setOnClickListener { onRegistrationTextViewClick()}
    }

    /*fun sendRequest(request: Request, onFailureListener: (call: Call, e: IOException) -> Unit, onResponseListener: (call: Call, response: Response) -> Unit) {
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = onFailureListener(call, e)
            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    333 -> {
                        val refreshCode = refresh()
                        if (refreshCode != 200) {
                            val reLoginCode = reLogin()
                            if (reLoginCode != 200) {
                                onFailureListener(call, IOException("Can't refresh token or reLogin"))
                            }
                        }
                        try {
                            val newCall = client.newCall(request)
                            val newResponse = newCall.execute()
                            if (newResponse.code == 333) {
                                throw IOException("Tokens die instantly")
                            }
                            else {
                                onResponseListener(newCall, newResponse)
                            }
                        }
                        catch (e: IOException) {
                            onFailureListener(call, e)
                        }
                    }
                }
                onResponseListener(call, response)
            }
        })
    }*/

    /*private fun login(login: String, password: String) {
        val url = MainActivity.rootUrl + "/auth/login"
        val body = AuthLoginData(login, password)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Login no response error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    try {
                        loginTokens = GsonBuilder().create().fromJson(
                            response.body!!.string(),
                            AuthLoginTokens::class.java
                        )
                        val jwt = JWT(loginTokens.accessToken)
                        loginRole = jwt.getClaim("role").asString()!!
                    } catch (e: Exception) {
                        MainActivity.mHandler.post {
                            Toast.makeText(context, "Response body interpretation error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    MainActivity.mHandler.post {
                        MainActivity.switchFragment(this, MainActivity.mainFragment)
                    }
                }
                else {
                    MainActivity.mHandler.post {
                        Toast.makeText(context, "Login response error: $response.code", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }*/

    /*private fun reLogin(): Int {
        val url = MainActivity.rootUrl + "/auth/login"
        val body = AuthLoginData(loginEmail!!, loginPassword!!)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        val response = client.newCall(request).execute()
        when (response.code) {
            200 -> {
                loginTokens = GsonBuilder().create().fromJson(
                    response.body!!.string(),
                    AuthLoginTokens::class.java
                )
            }
        }
        return response.code
    }*/

    /*private fun refresh(): Int {
        val url = MainActivity.rootUrl + "/auth/refrash"
        val body = AuthRefreshData(loginTokens.refrashToken)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        val response = client.newCall(request).execute()
        when (response.code) {
            200 -> {
                loginTokens = GsonBuilder().create().fromJson(
                    response.body!!.string(),
                    AuthLoginTokens::class.java
                )
            }
        }
        return response.code
    }*/

    private fun validateEmail(email: String): String {
        if (email.isEmpty()) return "Поле не должно быть пустым"
        //if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Неверый формат"
        return ""
    }

    private fun validatePassword(password: String): String {
        if (password.isEmpty()) return "Поле не должно быть пустым"
        return ""
    }

    private fun onLoginButtonClick() {
        if (emailValidity == null) emailValidity = validateEmail(emailEt.text.toString())
        if (passwordValidity == null) passwordValidity = validatePassword(passwordEt.text.toString())
        var validData = true

        if (emailValidity != "") {
            emailEt.error = emailValidity
            validData = false
        }
        if (passwordValidity != "") {
            passwordEt.error = passwordValidity
            validData = false
        }

        if (validData) {
            loginBn.startAnimation()
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            //login(email, password)
        }
        else {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            loginBn.startAnimation(shake)
            Toast.makeText(context, "Обнаружены некорректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRegistrationTextViewClick() {
        MainActivity.switchFragment(this, MainActivity.registrationFragment)
    }
}