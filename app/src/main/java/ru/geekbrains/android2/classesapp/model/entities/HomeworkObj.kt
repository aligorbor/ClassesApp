package ru.geekbrains.android2.classesapp.model.entities

data class HomeworkObj(
    var subjectId: String = "",
    var task: String = "",
    var daysLeft: Int = 0,
    var subjectObj: SubjectObj = SubjectObj()
)