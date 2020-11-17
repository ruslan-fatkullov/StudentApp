package com.example.studentass.models

class ScheduleDay(
    val id: Long,
    val number_day: Int,
    val group_id: Long,
    val numberWeek: Int,
    val coupels: List<ScheduleDayCouple>
)