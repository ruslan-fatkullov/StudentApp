package com.example.studentass.models

class WorkModel(
    val id: Long,
    val taskId: Long,
    val userId: Long,
    val fileIds: Array<Long>,
    val createdAt: Double,
    val updatedAt: Double,
    val teacherComment: String,
    val studentComment: String,
    val mark: String
)