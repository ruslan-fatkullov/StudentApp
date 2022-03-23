package com.example.studentass.services


import com.example.studentass.models.LiteratureData
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LiteratureApiService {

    companion object Factory {
        /*
        * Сборщик реализации интерфейса
        */
        fun create(): LiteratureApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()
            return retrofit.create(LiteratureApiService::class.java)
        }
    }


    /*
     * Получение списка литературы
     */
    //@Headers({"Authorization", "Bearer "+ token})
    @GET("literature/learning")
    fun getIdLiterature(@Header("Authorization") auth: String?, @Query("subjectId") subjectId: Long?, @Query("userid") userId: Int): Observable<List<LiteratureData>>
}