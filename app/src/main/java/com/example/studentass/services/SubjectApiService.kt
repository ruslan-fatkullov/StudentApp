package com.example.studentass.services


import com.example.studentass.models.*
import com.example.studentass.models.testResultModel.testResult
import com.example.studentass.models.testResultModel.toCheckModel
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    @GET("subject/all")
    fun getSubjectAll(@Header("Authorization") auth: String?): Observable<List<Subject>>

    /*
     * Получение списка предметов по id пользователя
     */
    @GET("subject/learning")
    fun getIdSubject(@Header("Authorization") auth: String?): Observable<List<Subject>>
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

    @GET("/testing/new/test")
    fun getTestByThemeId(@Header("Authorization") auth: String?, @Query("theme_id") theme_id: Long?, @Query("limit") limit: Long?): Observable<List<TestQuestion>>

    /*
         * Проверка теста
         */
    @POST("/testing/new/test/check")
    fun checkTest(@Header("Authorization") auth: String?, @Body requestBody: RequestBody): Observable<testResult>

    @GET("/testing/new/test/passed-themes")
    fun getPassedTests(@Header("Authorization") auth: String?, @Query("subj_id") subj_id: Long?, @Query("user_id") user_id: Long?): Observable<List<PassedTests>>


}