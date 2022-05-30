package com.example.studentass.services

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    fun logIn(@Body requestBody: RequestBody): Observable<ResponseBody>


}