package com.example.studentass.services


import com.example.studentass.models.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserApiService {

    companion object Factory {
        /*
         * Сборщик реализации интерфейса
         */
        fun create(): UserApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()
            return retrofit.create(UserApiService::class.java)
        }
    }

    /*
     * Получение списка предметов
     */
    @GET("user")
    fun getUser(@Header("Authorization") auth: String?): Observable<User>


}