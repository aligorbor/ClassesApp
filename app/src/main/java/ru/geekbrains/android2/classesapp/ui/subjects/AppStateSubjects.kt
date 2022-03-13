package ru.geekbrains.android2.classesapp.ui.subjects

import ru.geekbrains.android2.classesapp.model.entities.SubjectObj

sealed class AppStateSubjects {
    data class Success(val subjects: List<SubjectObj>) : AppStateSubjects()
    data class Error(val error: Throwable) : AppStateSubjects()
    object Loading : AppStateSubjects()
}
