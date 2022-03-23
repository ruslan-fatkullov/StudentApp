package com.example.studentass.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
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
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import kotlin.concurrent.thread


/*
 * Отвечает за авторизацию
 */
class LoginFragment : Fragment() {
    companion object {
        private val authApiService = AuthApiService.create()
        private val compositeDisposable = CompositeDisposable()
        private lateinit var context: Context                       // Статичные методы не имеют контекста, поэтому его нужно им передавать
        var token: String? = null
        //        var tokens: Tokens? = null                                  // Токены доступа и обновления
//            private set
        var role: String? = null                                    // Роль, извлекается из токенов
            private set

        fun init(context: Context) {
            LoginFragment.context = context
            readTokens()
        }


        /*
         * Возвращает поток с токенами, запрос не выполняется до вызова термиального оператора
         */
        fun logIn(login: String, password: String) : Observable<ResponseBody> {

            val jsonObject = JSONObject()
            jsonObject.put("login", login)
            jsonObject.put("password", password)
            val jsonObjectString = jsonObject.toString()
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            val responseBody = authApiService.logIn(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { t -> onLogInMap(t) }
            return responseBody
        }


        /*
         * Нужно для автоматического сохранения данных авторизации
         */
        private fun onLogInMap(r: ResponseBody): ResponseBody {
            val dsf = r.string()
            val parts = dsf.split('"')
            //Toast.makeText(Companion.context, "${parts[3]}", Toast.LENGTH_LONG).show()
            // Токены доступа и обновления
            token = parts[3]
            role = identifyRole(parts[3])
            thread {
                writeTokens(parts[3])
            }
            return r
        }


        /*
         * Возвращает пустой поток, запрос не выполняется до вызова термиального оператора
         */
        fun logOut() {
            thread {
                deleteTokens()
            }
        }
//            return authApiService.logOut()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .map { onLogOutMap() }



        /*
         * Нужно для автоматического удаления данных авторизации
         */
        private fun onLogOutMap() {
            thread {
                deleteTokens()
            }
        }


        /*
         * Возвращает поток токенов, запрос не выполняется до вызова термиального оператора
         */
//        fun refreshTokens(): Observable<Tokens> {
//            if (tokens == null)
//                throw RuntimeException("Refresh error: tokens are null")
//
//            return authApiService.refresh(tokens!!.refrashToken)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .map { t -> onRefreshTokensMap(t) }
//        }


        /*
         * Нужно для автоматической перезаписи данных авторизации
         */
//        private fun onRefreshTokensMap(r: Tokens): Tokens {
//            tokens = r
//            thread {
//                writeTokens()
//            }
//            return r
//        }


        /*
         * Извлекает роль из токена доступа
         */
        private fun identifyRole(accessToken: String): String {
            val role = JWT(accessToken)
                .getClaim("role")
                .asString()
                ?: throw RuntimeException("Role identification error: can't get claim")

            Toast.makeText(Companion.context, "id $role", Toast.LENGTH_LONG).show()
            return role
        }


        /*
         * Читает токены из постоянной памяти
         */
        private fun readTokens() {
            token = MemoryManager.loadTokens(context)
        }


        /*
         * Записывает токены в постоянную память
         */
        private fun writeTokens(token: String) {
            if (token == null)
                throw RuntimeException("Write tokens error: tokens are null")

            MemoryManager.saveTokens(context, token!!)
        }


        /*
         * Удаляет токены из постоянной памяти
         */
        private fun deleteTokens() {
            MemoryManager.deleteTokens(context)
        }
    }

    private var emailValidity: String? = null
    private var passwordValidity: String? = null


    /*
     * Заполнение фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    /*
     * Инициализация элементов интерфейса
     */
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


    /*
     * Управление видимостию панели действий
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.hide()
        }
    }


    /*
     * Очистка мусора, потенциально генерируемого запросами
     */
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


    /*
     * Вызывается при успешной авторизации
     */
    private fun onLogInSuccess() {
        val mainActivity = getAppCompatActivity<MainActivity>()
        if (mainActivity != null) {
              //mainActivity.switchSideways(MainFragment::class.java)
            when (role) {
                "USER" -> mainActivity.switchSideways(MainFragment::class.java)
                "TEACHER" -> mainActivity.switchSideways(MainFragment::class.java)
                else -> {
                    Toast.makeText(context, "Invalid role: $role", Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
        loginBn.revertAnimation()
    }


    /*
     * Вызывается при ошибке авторизации
     */
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


    /*
     * Проверяет почту
     */
    private fun validateEmail(email: String): String {
        if (email.isEmpty()) return "Поле не должно быть пустым"
        //if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Неверый формат"
        return ""
    }


    /*
     * Проверяет пароль
     */
    private fun validatePassword(password: String): String {
        if (password.isEmpty()) return "Поле не должно быть пустым"
        return ""
    }


    /*
     * Обработчик события нажатия на кнопку авторизации
     */
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


    /*
     * Обработчик события нажатия на кнопку перехода к регистрации
     */
    private fun onRegistrationTextViewClick() {
        getAppCompatActivity<MainActivity>()?.switchUp(RegistrationFragment::class.java)
    }



}