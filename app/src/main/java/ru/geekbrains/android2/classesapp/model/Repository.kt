package ru.geekbrains.android2.classesapp.model

import kotlinx.coroutines.flow.StateFlow
import ru.geekbrains.android2.classesapp.model.entities.ExamObj
import ru.geekbrains.android2.classesapp.model.entities.HomeworkObj
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj

interface Repository {
    suspend fun listSubject(): List<SubjectObj>
    suspend fun listHomework(): List<HomeworkObj>
    suspend fun listExam(): List<ExamObj>
    suspend fun listSchedule(): List<ScheduleObj>
    val ticker: StateFlow<Int>
    fun startTicker()
    fun stopTicker()
}