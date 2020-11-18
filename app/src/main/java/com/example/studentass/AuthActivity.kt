package com.example.studentass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class AuthActivity : AppCompatActivity() {
    companion object {
        const val credentialsLogin = "ritg"
        const val credentialsPassword = "ritg"

        val mHandler = Handler(Looper.getMainLooper())
        var loginTokens = AuthLoginTokens("", "")
        var loginRole = "NONE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
                mHandler.post {
                    Toast.makeText(applicationContext, "Login request error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val loginTokensObject = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginTokens::class.java)
                    loginTokens = loginTokensObject
                    val accessToken = loginTokens.accessToken
                    val jwt = JWT(accessToken)
                    loginRole = jwt.getClaim("role").asString()!!
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Response interpretation error: $e", Toast.LENGTH_LONG).show()
                }
                mHandler.post {
                    goToMainActivity()
                }
            }
        })
    }

    fun goToMainActivity() {
        val intentActivity = Intent(this, MainActivity::class.java)
        startActivity(intentActivity)
    }

    fun onButtonLoginClick(@Suppress("UNUSED_PARAMETER")view: View) {
        try {
            val loginText : String = editTextLogin?.text.toString()
            val passwordText : String = editTextPassword?.text.toString()

            if (loginText.isEmpty())
                throw Exception("Не указан адрес эл. почты")
            if (loginText.indexOf(' ', 0, true) != -1)
                throw Exception("Адрес эл. почты содержит недопустимые символы")
            if (passwordText.isEmpty())
                throw Exception("Не указан пароль")
            login(loginText, passwordText)
        }
        catch (e: Exception) {
            val errorMessage : String = "Ошибка: " + e.message
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}