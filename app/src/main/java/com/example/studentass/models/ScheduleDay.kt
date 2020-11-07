package com.example.studentass.models

class ScheduleDay(
    val id : Int,
    val number_day : Int,
    val group_id : Int,
    val numberWeek : Int,
    val coupels : List<ScheduleDayCouple>
)