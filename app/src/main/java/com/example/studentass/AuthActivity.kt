package com.example.studentass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentass.AuthActivity.Companion.mHandler
import com.example.studentass.models.AuthLoginQuery
import com.example.studentass.models.AuthLoginResponse
import com.example.studentass.models.Schedule
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.attribute.AclEntry.newBuilder

class AuthActivity : AppCompatActivity() {
    companion object {
        val mHandler : Handler = Handler(Looper.getMainLooper())
    }

    private var loginTokens: AuthLoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun login() {
        val url = "http://test.asus.russianitgroup.ru/api/auth/login"
        val credentialsLogin = "ritg"
        val credentialsPassword = "ritg"
        val body = AuthLoginQuery("student1", "123456")

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Login request error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                mHandler.post {
                    try {
                        val loginTokensObject = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginResponse::class.java)
                        loginTokens = loginTokensObject
                        Toast.makeText(applicationContext, "Success: ${loginTokens!!.accessToken}", Toast.LENGTH_LONG).show()
                    } catch (e : Exception) {
                        Toast.makeText(applicationContext, "Login init error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun onButtonLoginClick(view: View) {
        try {
            val email : String = editTextEmail?.text.toString()
            val password : String = editTextPassword?.text.toString()

            if (email.isEmpty())
                throw Exception("Не указан адрес эл. почты")
            if (email.indexOf(' ', 0, true) != -1)
                throw Exception("Адрес эл. почты содержит недопустимые символы")
            if (password.isEmpty())
                throw Exception("Не указан пароль")

            login()
            /*val message : String = "Почта: " + email + "    Пароль: " + password
            val intentActivity = Intent(this, MainActivity::class.java)
            intentActivity.putExtra(MainActivity.MESSAGE, message)
            startActivity(intentActivity)*/
        }
        catch (e: Exception) {
            val errorMessage : String = "Ошибка: " + e.message
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


}