package com.example.studentass.services


import com.example.studentass.models.Subject
import com.example.studentass.models.TaskModel
import com.example.studentass.models.TestThemesData
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SubjectApiService  {

    companion object Factory {
        /*
         * Сборщик реализации интерфейса
         */
        fun create(): SubjectApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.5:8080")
                .build()
            return retrofit.create(SubjectApiService::class.java)
        }
    }

    /*
     * Получение списка предметов
     */
    //@Headers({"Authorization", "Bearer "+ token})
    @GET("subject/all")
    fun getSubjectAll(@Header("Authorization") auth: String?): Observable<List<Subject>>

    /*
     * Получение списка предметов по id пользователя
     */
    @GET("subject/learning")
    fun getIdSubject(@Header("Authorization") auth: String?): Observable<List<Subject>>
//    /*
//         * Получение списка предметов по id пользователя
//         */
//    @GET("subject/learning")
//    fun getIdSubject(@Header("Authorization") auth: String?, @Query("userid") userid: Int): Observable<List<Subject>>

    /*
     * Получение списка тем тестов
     */
    @GET("/api/testing/themes")
    fun getIdThemes(@Header("Authorization") auth: String?, @Query("subj_id") subject_id: Long?): Observable<List<TestThemesData>>

    /*
     * Получение списка заданий
     */
    @POST("/task/criteria-search")
    fun getIdTask(@Header("Authorization") auth: String?, @Body requestBody: RequestBody): Observable<List<TaskModel>>


}