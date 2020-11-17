package com.example.studentass.models

class Schedule (
    val id: Long,
    val nameGroup: String,
    val url_group: String,
    val days: List<ScheduleDay>
)