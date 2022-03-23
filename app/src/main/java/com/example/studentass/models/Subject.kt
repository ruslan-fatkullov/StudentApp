package com.example.studentass.models

class Subject (
    val id: Long,
    val name: String,
    val decryption: String,
    val createdAt: Double,
    val updatedAt: Double,
    val themeIds: Array<Long>,
    val teacherIds: Array<Long>,
    val semesterIds: Array<Long>,
    val departmentId: Long,
    val questionIds: Array<Long>,
)