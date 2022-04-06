package com.example.studentass.models

class User(
    val id: Long,
    val login: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val phone: String,
    val avatarId: Long,
    val userContactIds: ArrayList<Long>,
    val studyGroupId: Long,
    val departmentId: Long,
    val role: String
)