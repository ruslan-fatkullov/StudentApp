package com.example.studentass.models

class TaskModel (
    val id: Long,
    val type: String,
    val title: String,
    val description: String,
    val fileIds: Array<Long>,
    val semesterIds: Array<Long>,
    val workIds: Array<Long>,
    val userId: Long,
)
