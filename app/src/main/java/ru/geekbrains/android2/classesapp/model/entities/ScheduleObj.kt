package ru.geekbrains.android2.classesapp.model.entities

import java.util.*

data class ScheduleObj(
    var dayOfWeek: Int = 0,
    var order: Int = 0,
    var fromDate: Date = Date(),
    var toDate: Date = Date(),
    var subjectId: String = "",
    var subjectObj: SubjectObj = SubjectObj()
)