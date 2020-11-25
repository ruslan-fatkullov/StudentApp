package com.example.studentass.fragments

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
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.example.studentass.models.AuthRefreshData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

class LoginFragment : Fragment() {
    private val credentialsLogin = "ritg"
    private val credentialsPassword = "ritg"

    private lateinit var loginEmail: String
    private lateinit var loginPassword: String

    var loginTokens = AuthLoginTokens("", "")
    var loginRole = "NONE"

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
    }

    private fun executeRequest(request: Request): Response {
        val response = client.newCall(request).execute()
        checkResponseCode(response.code)
        return response
    }

    private fun executeJwtRequest(request: Request, onUpgradeListener: (Request.Builder) -> Request): Response {
        var response: Response

        try {
            response = client.newCall(request).execute()
            checkResponseCode(response.code)
        }
        catch (e: UpgradeRequiredException) {
            try {
                executeRefresh()
            }
            catch (e: UnauthorizedException) {
                executeLogin()
            }
            val newRequest = onUpgradeListener(request.newBuilder())
            response = client.newCall(newRequest).execute()
            checkResponseCode(response.code)
        }

        return response
    }

    private fun executeRefresh(): Response {
        val url = MainActivity.rootUrl + "/auth/refrash"
        val body = AuthRefreshData(loginTokens.refrashToken)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

        val response = client.newCall(request).execute()
        checkResponseCode(response.code)

        loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginTokens::class.java)

        return response
    }

    private fun executeLogin(): Response {
        val url = MainActivity.rootUrl + "/auth/login"
        val body = AuthLoginData(loginEmail, loginPassword)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()

        val response = client.newCall(request).execute()
        checkResponseCode(response.code)

        loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), AuthLoginTokens::class.java)
        val jwt = JWT(loginTokens.accessToken)
        loginRole = jwt.getClaim("role").asString()!!

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
                        "student" -> MainActivity.switchFragment(this, MainActivity.mainFragment)
                        else -> Toast.makeText(context, "Invalid role: $loginRole", Toast.LENGTH_LONG).show()
                    }
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
        MainActivity.switchFragment(this, MainActivity.registrationFragment)
    }
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

open class RequestCodeException(message: String) : Exception(message) // родительский класс ошибок запросов
class BadRequestException(message: String) : RequestCodeException(message) // 400
class UnauthorizedException(message: String) : RequestCodeException(message) // 401
class UpgradeRequiredException(message: String) : RequestCodeException(message)// 426
class InternalServerErrorException(message: String) : RequestCodeException(message) // 500