package com.example.studentass.services


import com.example.studentass.models.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WorkApiService {

    companion object Factory {
        /*
         * Сборщик реализации интерфейса
         */
        fun create(): WorkApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()
            return retrofit.create(WorkApiService::class.java)
        }
    }

    /*
     * Получение группы
     */
    @POST("/work/criteria-search")
    fun getWorkByCriteria(
        @Header("Authorization") auth: String?,
        @Body requestBody: RequestBody
    ): Observable<List<WorkModel>>


}