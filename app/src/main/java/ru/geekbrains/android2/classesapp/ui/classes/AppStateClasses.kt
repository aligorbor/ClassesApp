package ru.geekbrains.android2.classesapp.ui.classes

import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj

sealed class AppStateClasses {
    data class SuccessSubjects(val classes: List<SubjectObj>) : AppStateClasses()
    data class Success(val classes: List<ScheduleObj>, val scrollToPos: Int) : AppStateClasses()
    data class Error(val error: Throwable) : AppStateClasses()
    object Loading : AppStateClasses()
}