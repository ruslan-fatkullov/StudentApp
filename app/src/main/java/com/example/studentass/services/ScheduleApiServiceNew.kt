package com.example.studentass.services


import com.example.studentass.models.*
import com.example.studentass.models.ScheduleNew.SubjectList
import com.example.studentass.models.ScheduleNew.Timetable
import com.example.studentass.models.testResultModel.testResult
import com.example.studentass.models.testResultModel.toCheckModel
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ScheduleApiServiceNew  {

    companion object Factory {
        /*
         * Сборщик реализации интерфейса
         */
        fun create(): ScheduleApiServiceNew {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://time.ulstu.ru")
                .build()
            return retrofit.create(ScheduleApiServiceNew::class.java)
        }
    }

    /*
  * Получение списка групп
  */
    @GET("api/1.0/groups")
    fun getGroupList(): Observable<SubjectList>


    /*
     * Получение расписание группы
     */
    @GET("api/1.0/timetable")
    fun getGroupSchedule(@Query("filter") groupName: String): Observable<Timetable>


}