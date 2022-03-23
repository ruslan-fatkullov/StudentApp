package com.example.studentass.models

class LiteratureData (
    val id: Long,
    val type: String,
    val title: String,
    val authors: String,
    val description: String,
    val fileIds: List<Long>,
    val userId: Long,
    val semesterIds: List<Long>
)