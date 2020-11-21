package com.example.studentass.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginFragment : Fragment() {
    companion object {
        const val credentialsLogin = "ritg"
        const val credentialsPassword = "ritg"

        var loginTokens = AuthLoginTokens("", "")
        var loginRole = "NONE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBn.setOnClickListener { _ -> onButtonLoginClick() }
    }

    private fun login(login: String, password: String) {
        val url = "http://test.asus.russianitgroup.ru/api/auth/login"
        val body = AuthLoginData(login, password)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Login request error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val loginTokensObject = GsonBuilder().create().fromJson(
                        response.body!!.string(),
                        AuthLoginTokens::class.java
                    )
                    loginTokens = loginTokensObject
                    val accessToken = loginTokens.accessToken
                    val jwt = JWT(accessToken)
                    loginRole = jwt.getClaim("role").asString()!!
                    MainActivity.mHandler.post {
                        //goToMainActivity()
                    }
                } catch (e: Exception) {
                    MainActivity.mHandler.post {
                        Toast.makeText(context, "Response interpretation error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    /*fun goToMainActivity() {
        val intentActivity = Intent(this, MainActivity::class.java)
        startActivity(intentActivity)
    }*/

    private fun onButtonLoginClick() {
        try {
            val emailText : String = emailTv?.text.toString()
            val passwordText : String = passwordTv?.text.toString()

            if (emailText.isEmpty())
                throw Exception("Не указан адрес эл. почты")
            if (passwordText.isEmpty())
                throw Exception("Не указан пароль")

            loginBn.startAnimation()
            //login(emailText, passwordText)
            loginRole = "student"
            (activity as MainActivity).goToMainFragment(this)
        }
        catch (e: Exception) {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            loginBn.startAnimation(shake)

            val errorMessage : String = "Ошибка: " + e.message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}