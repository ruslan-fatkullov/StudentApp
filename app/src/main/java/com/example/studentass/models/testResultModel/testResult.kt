package com.example.studentass.models.testResultModel

class testResult(
    val questions: List<questionsResult>,
    val rating: Long,
    val passedAt: Double
)

class questionsResult(
    val questionData: questionDataResult,
    val userAnswers: List<userAnswerResult>
)

class questionDataResult(
    val id: Long,
    val question: String,
    val questionType: String,
    val complexity: Double,
    val fileIds: List<Long>,
    val answerList: List<AnswerResult>
)


class userAnswerResult(
    val answerId: Long,
    val right: Boolean
)

class AnswerResult(
    val id: Long,
    val answer: String,
    val right: Boolean,
    val fileIds: List<Long>
)


