package com.example.studentass.services


import com.example.studentass.models.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GroupApiService {

    companion object Factory {
        /*
         * Сборщик реализации интерфейса
         */
        fun create(): GroupApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()
            return retrofit.create(GroupApiService::class.java)
        }
    }

    /*
     * Получение группы
     */
    @GET("study-group")
    fun getGroupById(
        @Header("Authorization") auth: String?,
        @Query("id") groupId: Long
    ): Observable<Group>


}