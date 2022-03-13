package ru.geekbrains.android2.classesapp.ui.home

import ru.geekbrains.android2.classesapp.model.entities.ExamObj
import ru.geekbrains.android2.classesapp.model.entities.HomeworkObj
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj

sealed class AppStateHome {
    data class SuccessClasses(
        val classes: List<ScheduleObj>,
        val numberClasses: Int,
        val scrollToPos: Int,
        val isToday: Boolean
    ) : AppStateHome()

    data class SuccessSubjects(val classes: List<SubjectObj>) : AppStateHome()
    data class SuccessHomeworks(val homeworks: List<HomeworkObj>) : AppStateHome()
    data class SuccessExams(val exams: List<ExamObj>) : AppStateHome()
    object SuccessExamsTimer : AppStateHome()
    data class Error(val error: Throwable) : AppStateHome()
    object Loading : AppStateHome()
}
