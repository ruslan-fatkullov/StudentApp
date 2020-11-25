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
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

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

    /*fun executeRequest(request: Request): Response {
        val client = OkHttpClient()
        var response: Response
        try {
            try {
                response = client.newCall(request).execute()
            }
            catch (e: IOException) {
                throw IOException("no response ($e)")
            }
            var responseCode = response.code
            if (responseCode != 200) {
                if (responseCode == 426) {
                    val refreshResponse: Response
                    try {
                        refreshResponse = executeRefresh(loginTokens.refrashToken)
                    }
                    catch (e: IOException) {
                        throw IOException("refresh error ($e)")
                    }
                    val refreshResponseCode = refreshResponse.code
                    if (refreshResponseCode != 200) {
                        if (refreshResponseCode == 401) {
                            val reLoginResponse: Response
                            if (loginEmail == null || loginPassword == null) {
                                throw IOException("login error (no data to send)")
                            }
                            try {
                                reLoginResponse = executeLogin(loginEmail!!, loginPassword!!)
                            }
                            catch (e: IOException) {
                                throw IOException("login error ($e)")
                            }
                            val reLoginResponseCode = reLoginResponse.code
                            if (reLoginResponseCode != 200) {
                                throw IOException("login response code: $reLoginResponseCode")
                            }
                        }
                        else {
                            throw IOException("refresh response code: $refreshResponseCode")
                        }
                    }
                    try {
                        response = client.newCall(request).execute()
                    }
                    catch (e: IOException) {
                        throw IOException("no response ($e)")
                    }
                    responseCode = response.code
                    if (responseCode != 200) {
                        throw IOException("duplicated response code: $responseCode")
                    }
                }
                else {
                    throw IOException("response code: $responseCode")
                }
            }
        }
        catch (e: IOException) {
            throw IOException("Request error (${e.message})")
        }
        return response
    }*/

    /*private fun executeRefresh(refreshToken: String): Response {
        val url = MainActivity.rootUrl + "/auth/refrash"
        val body = AuthRefreshData(refreshToken)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

        return client.newCall(request).execute()
    }*/

    /*private fun executeLogin(email: String, password: String): Response {
        val url = MainActivity.rootUrl + "/auth/login"
        val body = AuthLoginData(email, password)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

        return client.newCall(request).execute()
    }*/
    private fun executeLogin(email: String, password: String): Response {
        val url = MainActivity.rootUrl + "/auth/login"
        val body = AuthLoginData(email, password)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

        val response = client.newCall(request).execute()

        checkResponseCode(response.code)

        return response
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
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()

            thread {
                try {
                    executeLogin(email, password)
                }
                catch (e: IOException) {
                    val errorMessage = when (e) {
                        is UnauthorizedException -> "Неверные почта и/или пароль"
                        else -> "Ошибка подключения: $e (${e.message})"
                    }
                    MainActivity.mHandler.post {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        loginBn.revertAnimation()
                    }
                    return@thread
                }
                loginEmail = email
                loginPassword = password
                MainActivity.switchFragment(this, MainActivity.mainFragment)
            }
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

private fun checkResponseCode(code: Int) {
    if (code == 200) return
    throw when (code) {
        400 -> BadRequestException("плохой запрос")
        401 -> UnauthorizedException("не авторизован")
        426 -> UpgradeRequiredException("необходимо обновление")
        500 -> InternalServerErrorException("внутренняя ошибка сервера")
        else -> DefaultCodeException("код $code")
    }
}

class DefaultCodeException(message: String) : IOException(message) // unknown
class BadRequestException(message: String) : IOException(message) // 400
class UnauthorizedException(message: String) : IOException(message) // 401
class UpgradeRequiredException(message: String) : IOException(message)// 426
class InternalServerErrorException(message: String) : IOException(message) // 500