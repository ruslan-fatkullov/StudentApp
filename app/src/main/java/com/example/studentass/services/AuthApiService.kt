package com.example.studentass.services

import com.example.studentass.models.Tokens
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


/*
 * Интерфейс сервиса авторизации
 */
interface AuthApiService {
    companion object Factory {


        /*
         * Сборщик реализации интерфейса
         */

        fun create(): AuthApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()

            return retrofit.create(AuthApiService::class.java)
        }

    }

    /*
     * Авторизация
     */
    @POST("login")
    fun logIn(@Body requestBody: RequestBody):Observable<ResponseBody>
//    /*
//     * Авторизация
//     */
//    @FormUrlEncoded
//    @POST("login")
//    fun logIn(
//        @Field("login") login: String,
//        @Field("password") password: String
//    ):Observable<Tokens>




    /*
     * Обновление токенов
     */
    @FormUrlEncoded
    @POST("api/auth/refrash")
    fun refresh(@Field("refrashToken") refreshToken: String): Observable<Tokens>



    /*
     * Выход
     */
    @POST("api/auth/logout")
    fun logOut(): Observable<Unit>



}