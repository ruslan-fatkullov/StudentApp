package com.example.studentass.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.auth0.android.jwt.JWT
import com.example.studentass.MainActivity
import com.example.studentass.MainActivity.Companion.client
import com.example.studentass.MainActivity.Companion.mainActivity
import com.example.studentass.MainActivity.Companion.rootUrl
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.example.studentass.models.AuthRefreshData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

class LoginFragment : Fragment() {
    companion object {
        private const val credentialsLogin = "ritg"
        private const val credentialsPassword = "ritg"

        private var loginEmail: String? = null
        private var loginPassword: String? = null

        var loginTokens: AuthLoginTokens? = null
        var loginRole = "invalid"

        fun executeRequest(request: Request): Response {
            val response = client.newCall(request).execute()
            checkResponseCode(response.code)
            return response
        }

        fun executeJwtRequest(requestBuilder: Request.Builder): Response {
            var response: Response
            requestBuilder.addHeader("Authorization", loginTokens!!.accessToken)

            try {
                response = client.newCall(requestBuilder.build()).execute()
                checkResponseCode(response.code)
            }
            catch (e: UpgradeRequiredException) {
                try {
                    executeRefresh()
                }
                catch (e: UnauthorizedException) {
                    executeLogin()
                }
                requestBuilder.removeHeader("Authorization").addHeader("Authorization", loginTokens!!.accessToken)
                response = client.newCall(requestBuilder.build()).execute()
                checkResponseCode(response.code)
            }

            return response
        }

        private fun executeRefresh(): Response {
            if (loginTokens == null) {
                throw NoDataException("Refresh token is missing")
            }

            val url = "$rootUrl/auth/refrash"
            val body = AuthRefreshData(loginTokens!!.refrashToken)

            val credential = Credentials.basic(credentialsLogin, credentialsPassword)
            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginTokens::class.java)

            return response
        }

        fun executeLogin(): Response {
            if (loginEmail == null || loginPassword == null) {
                throw NoDataException("Login data is missing")
            }

            val url = "$rootUrl/auth/login"
            //val url = "https://4b7af1df-c62e-49e5-b0a5-929837fb7e36.mock.pstmn.io/api/auth/login"
            val body = AuthLoginData(loginEmail!!, loginPassword!!)

            val credential = Credentials.basic(credentialsLogin, credentialsPassword)
            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginTokens::class.java)
            val jwt = JWT(loginTokens!!.accessToken)
            loginRole = jwt.getClaim("role").asString()!!

            return response
        }

        fun executeLogout(): Response {
            if (loginTokens == null) {
                throw NoDataException("Refresh token is missing")
            }

            val url = "$rootUrl/auth/logout"
            val body = AuthRefreshData(loginTokens!!.refrashToken)

            val credential = Credentials.basic(credentialsLogin, credentialsPassword)
            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            return response
        }

        fun saveLoginData(mainActivity: MainActivity) {
            val sharedPreferences = mainActivity.getSharedPreferences("LoginData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("loginEmail", loginEmail)
                putString("loginPassword", loginPassword)
                putString("accessToken", loginTokens!!.accessToken)
                putString("refrashToken", loginTokens!!.refrashToken)
            }.apply()
        }

        fun loadLoginData(mainActivity: MainActivity) {
            val sharedPreferences = mainActivity.getSharedPreferences("LoginData", Context.MODE_PRIVATE)
            sharedPreferences.apply {
                loginEmail = getString("loginEmail", null)
                loginPassword = getString("loginPassword", null)
                val accessToken = getString("accessToken", null)
                val refrashToken = getString("refrashToken", null)
                if (accessToken != null && refrashToken != null) {
                    loginTokens = AuthLoginTokens(accessToken, refrashToken)
                }
            }
        }

        fun deleteLoginData(mainActivity: MainActivity) {
            val sharedPreferences = mainActivity.getSharedPreferences("LoginData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().apply()
        }

        private fun checkResponseCode(code: Int) {
            if (code in 100..399) return
            throw when (code) {
                400 -> BadRequestException("плохой запрос")
                401 -> UnauthorizedException("не авторизован")
                426 -> UpgradeRequiredException("необходимо обновление")
                500 -> InternalServerErrorException("внутренняя ошибка сервера")
                else -> RequestCodeException("код $code")
            }
        }

        class NoDataException(message: String) : Exception(message)

        open class RequestCodeException(message: String) : Exception(message) // родительский класс ошибок запросов
        class BadRequestException(message: String) : RequestCodeException(message) // 400
        class UnauthorizedException(message: String) : RequestCodeException(message) // 401
        class UpgradeRequiredException(message: String) : RequestCodeException(message)// 426
        class InternalServerErrorException(message: String) : RequestCodeException(message) // 500
    }

    private var emailValidity: String? = null
    private var passwordValidity: String? = null

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
        onHiddenChanged(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mainActivity.actionBar.hide()
        }
    }

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
            loginEmail = emailEt.text.toString()
            loginPassword = passwordEt.text.toString()

            thread {
                try {
                    executeLogin()
                }
                catch (e: Exception) {
                    val errorMessage = when (e) {
                        is UnauthorizedException -> "Неверные почта и/или пароль"
                        is IOException -> "Ошибка подключения: $e (${e.message})"
                        else -> "Неизвестная ощибка подключения: $e (${e.message})"
                    }
                    MainActivity.mHandler.post {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        loginBn.revertAnimation()
                    }
                    return@thread
                }
                MainActivity.mHandler.post {
                    when (loginRole) {
                        "student" -> mainActivity.switchFragment(MainFragment::class.java)
                        else -> {
                            Toast.makeText(context, "Invalid role: $loginRole", Toast.LENGTH_LONG).show()
                            return@post
                        }
                    }
                    saveLoginData(mainActivity)
                    loginBn.revertAnimation()
                }
            }
        }
        else {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            loginBn.startAnimation(shake)
            Toast.makeText(context, "Обнаружены некорректные данные", Toast.LENGTH_LONG).show()
        }
    }

    private fun onRegistrationTextViewClick() {
        mainActivity.switchFragment(RegistrationFragment::class.java, false)
    }
}