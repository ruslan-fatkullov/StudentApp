package com.example.studentass.models

class TestQuestionModel (
    val id: Long,
    val question: String,
    val questionType: String,
    val complexity: Long,
    val fileIds: ArrayList<Long>,
    val answers: ArrayList<Any>
)
