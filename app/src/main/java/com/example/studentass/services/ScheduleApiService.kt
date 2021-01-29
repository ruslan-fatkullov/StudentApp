package com.example.studentass.services

import com.example.studentass.models.Schedule
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/*
 * Интерфейс сервиса расписания
 */
interface ScheduleApiService {
    companion object Factory {


        /*
         * Сборщик реализации интерфейса
         */
        fun create(): ScheduleApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://4b7af1df-c62e-49e5-b0a5-929837fb7e36.mock.pstmn.io/")
                .build()
            return retrofit.create(ScheduleApiService::class.java)
        }
    }


    /*
     * Получение списка групп
     */
    @GET("api/schedule/group-list")
    fun getGroupList(): Observable<Array<String>>


    /*
     * Получение расписание группы
     */
    @GET("api/schedule/group")
    fun getGroupSchedule(@Query("nameGroup") groupName: String): Observable<Schedule>
}