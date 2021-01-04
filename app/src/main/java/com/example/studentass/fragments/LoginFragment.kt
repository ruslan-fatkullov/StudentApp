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
import com.example.studentass.R
import com.example.studentass.common.MemoryManager
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Tokens
import com.example.studentass.services.AuthApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.concurrent.thread

class LoginFragment : Fragment() {
    companion object {
        private val authApiService = AuthApiService.create()
        private val compositeDisposable = CompositeDisposable()
        private lateinit var context: Context
        var tokens: Tokens? = null
            private set
        var role: String? = null
            private set

        fun init(context: Context) {
            LoginFragment.context = context
            readTokens()
        }

        fun logIn(login: String, password: String): Observable<Tokens> {
            return authApiService.logIn(login, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { t -> onLogInMap(t) }
        }

        private fun onLogInMap(r: Tokens): Tokens {
            tokens = r
            role = identifyRole(r.accessToken)
            thread {
                writeTokens()
            }
            return r
        }

        fun logOut(): Observable<Unit> {
            return authApiService.logOut()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { onLogOutMap() }
        }

        private fun onLogOutMap() {
            thread {
                deleteTokens()
            }
        }

        fun refreshTokens(): Observable<Tokens> {
            if (tokens == null)
                throw RuntimeException("Refresh error: tokens are null")

            return authApiService.refresh(tokens!!.refrashToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { t -> onRefreshTokensMap(t) }
        }

        private fun onRefreshTokensMap(r: Tokens): Tokens {
            tokens = r
            thread {
                writeTokens()
            }
            return r
        }

        private fun identifyRole(accessToken: String): String {
            return JWT(accessToken)
                .getClaim("role")
                .asString()
                ?: throw RuntimeException("Role identification error: can't get claim")
        }

        private fun readTokens() {
            tokens = MemoryManager.loadTokens(context)
        }

        private fun writeTokens() {
            if (tokens == null)
                throw RuntimeException("Write tokens error: tokens are null")

            MemoryManager.saveTokens(context, tokens!!)
        }

        private fun deleteTokens() {
            MemoryManager.deleteTokens(context)
        }


        /*fun executeRequest(requestBuilder: Request.Builder): Response {
            val client = OkHttpClient()
            val response = client.newCall(requestBuilder.build()).execute()
            checkResponseCode(response.code)
            return response
        }

        fun executeJwtRequest(requestBuilder: Request.Builder): Response {
            val client = OkHttpClient()
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
            val client = OkHttpClient()

            val body = AuthRefreshData(loginTokens!!.refrashToken)

            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().method("POST", requestBody).url(refreshUrl).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), Tokens::class.java)

            return response
        }

        fun executeLogin(): Response {
            if (loginEmail == null || loginPassword == null) {
                throw NoDataException("Login data is missing")
            }
            val client = OkHttpClient()

            //val url = "https://4b7af1df-c62e-49e5-b0a5-929837fb7e36.mock.pstmn.io/api/auth/login"
            val body = AuthLoginData(loginEmail!!, loginPassword!!)

            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().method("POST", requestBody).url(loginUrl).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            loginTokens = GsonBuilder().create().fromJson(response.body!!.string(), Tokens::class.java)
            val jwt = JWT(loginTokens!!.accessToken)
            loginRole = jwt.getClaim("role").asString()!!

            return response
        }

        fun executeLogout(): Response {
            if (loginTokens == null) {
                throw NoDataException("Refresh token is missing")
            }
            val client = OkHttpClient()

            val body = AuthRefreshData(loginTokens!!.refrashToken)

            val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

            val request = Request.Builder().method("POST", requestBody).url(logoutUrl).build()

            val response = client.newCall(request).execute()
            checkResponseCode(response.code)

            return response
        }*/



        private fun checkResponseCode(code: Int) {
            if (code in 100..399) return
            throw when (code) {
                400 -> BadRequestException("плохой запрос ($code)")
                401 -> UnauthorizedException("не авторизован ($code)")
                426 -> UpgradeRequiredException("необходимо обновление ($code)")
                500 -> InternalServerErrorException("внутренняя ошибка сервера ($code)")
                502 -> BadGatewayException("плохой шлюз ($code)")
                else -> RequestCodeException("код $code")
            }
        }

        open class RequestCodeException(message: String) : Exception(message) // родительский класс ошибок запросов
        class BadRequestException(message: String) : RequestCodeException(message) // 400
        class UnauthorizedException(message: String) : RequestCodeException(message) // 401
        class UpgradeRequiredException(message: String) : RequestCodeException(message)// 426
        class InternalServerErrorException(message: String) : RequestCodeException(message) // 500
        class BadGatewayException(message: String) : RequestCodeException(message) // 502
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
            getAppCompatActivity<MainActivity>()?.actionBar?.hide()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun onLogInSuccess() {
        val mainActivity = getAppCompatActivity<MainActivity>()
        if (mainActivity != null) {
            when (role) {
                "student" -> mainActivity.switchSideways(MainFragment::class.java)
                else -> {
                    Toast.makeText(context, "Invalid role: $role", Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
        loginBn.revertAnimation()
    }

    private fun onLogInError(e: Throwable) {
        loginBn.revertAnimation()
        /*val errorMessage = when (e) {
            is UnauthorizedException -> "Неверные почта и/или пароль"
            is IOException -> "Ошибка подключения: $e (${e.message})"
            else -> "Неизвестная ошибка подключения: $e (${e.message})"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()*/
        Toast.makeText(Companion.context, "LogIn error: $e", Toast.LENGTH_LONG).show()
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
        val login = emailEt.text.toString()
        val password = passwordEt.text.toString()

        if (emailValidity == null) emailValidity = validateEmail(login)
        if (passwordValidity == null) passwordValidity = validatePassword(password)
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

            compositeDisposable.add(
                logIn(login, password)
                    .subscribe (
                        { this.onLogInSuccess() },
                        { e -> this.onLogInError(e) }
                    )
            )
        }
        else {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            loginBn.startAnimation(shake)
            Toast.makeText(context, "Обнаружены некорректные данные", Toast.LENGTH_LONG).show()
        }
    }

    private fun onRegistrationTextViewClick() {
        getAppCompatActivity<MainActivity>()?.switchUp(RegistrationFragment::class.java)
    }
}