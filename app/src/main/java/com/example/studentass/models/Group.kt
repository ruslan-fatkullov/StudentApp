package com.example.studentass.models

class Group(

    val id: Long,
    val code: Long,
    val groupNumber: Long,
    val numberOfSemester: Long,
    val shortName: String,
    val fullName: String,
    val createdAt: Double,
    val updatedAt: Double,
    val yearOfStudyStart: Long,
    val departmentId: Long,
    val studentIds: ArrayList<Long>,
    val subjectSemesterIds: ArrayList<Long>,

    )