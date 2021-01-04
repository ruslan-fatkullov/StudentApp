package com.example.studentass.services

import com.example.studentass.models.Tokens
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
    companion object Factory {
        fun create(): AuthApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://4b7af1df-c62e-49e5-b0a5-929837fb7e36.mock.pstmn.io/")
                .build()
            return retrofit.create(AuthApiService::class.java)
        }
    }

    @FormUrlEncoded
    @POST("api/auth/login")
    fun logIn(@Field("login") login: String,
              @Field("password") password: String): Observable<Tokens>

    @FormUrlEncoded
    @POST("api/auth/refrash")
    fun refresh(@Field("refrashToken") refreshToken: String): Observable<Tokens>

    @POST("api/auth/logout")
    fun logOut(): Observable<Unit>
}