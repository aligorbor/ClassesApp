package ru.geekbrains.android2.classesapp.model.entities

import java.util.*

data class ExamObj(
    var subjectId: String = "",
    var dateExam: Date = Date(),
    var daysLeft: Int = 0,
    var subjectObj: SubjectObj = SubjectObj()
)
