package com.example.studentass.models

class ScheduleDayCouple(
    val id : Long,
    val day_id : Long,
    val pair_number : Int,
    val teacher : String,
    val subject : String,
    val subgroup : Int,
    val place : String,
    val typeSubject : Int,
    val info : String
)