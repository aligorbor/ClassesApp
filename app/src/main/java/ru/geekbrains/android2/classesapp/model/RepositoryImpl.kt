package ru.geekbrains.android2.classesapp.model

import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.geekbrains.android2.classesapp.model.entities.ExamObj
import ru.geekbrains.android2.classesapp.model.entities.HomeworkObj
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj
import ru.geekbrains.android2.classesapp.utils.daysLeft
import ru.geekbrains.android2.classesapp.utils.daysLeftHw
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositoryImpl() : Repository {
    private var mapSubjects: MutableMap<String, SubjectObj> = mutableMapOf()
    private var listOfExams = mutableListOf<ExamObj>()
    private var listOfClasses = mutableListOf<ScheduleObj>()
    private val scope: CoroutineScope =
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(0)
    override val ticker: StateFlow<Int> = mutableTicker


    override fun startTicker() {
        if (job == null) startJob()
    }

    private fun startJob() {
        job = scope.launch {
            var counter = 1
            while (isActive) {
                delay(TIMER_DELAY)
                mutableTicker.value = counter++
            }
        }
    }

    override fun stopTicker() {
        scope.coroutineContext.cancelChildren()
        job = null
        mutableTicker.value = 0
    }


    override suspend fun listSubject(): List<SubjectObj> {
        var subjects: List<SubjectObj> = listOf()
        val query = ParseQuery.getQuery<ParseObject>(CLASS_SUBJECT)
        query.orderByDescending("obligatory").addAscendingOrder("name")
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null) {
                    subjects = getListParseSubject(objects)
                    mapSubjects.clear()
                    subjects.associateTo(mapSubjects) {
                        it.name to it
                    }
                }
                cont.resume(subjects)
            }
        }
    }

    private fun getListParseSubject(objects: List<ParseObject>) = objects.map {
        getParseSubject(it)
    }

    private fun getParseSubject(obj: ParseObject) = SubjectObj(
        name = obj.getString("name") ?: "",
        teacher = obj.getString("teacher") ?: "",
        about = obj.getString("about") ?: "",
        obligatory = obj.getBoolean("obligatory") ?: true,
        imageId = obj.getNumber("imageid")?.toInt() ?: 0,
        openIn = obj.getBoolean("openin") ?: false
    )

    private suspend fun getListSchedule(): List<ScheduleObj> {
        var schedule: List<ScheduleObj> = listOf()
        val query = ParseQuery.getQuery<ParseObject>(CLASS_SCHEDULE)
        query.orderByAscending("dayofweek").addAscendingOrder("order")
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null) schedule = getListParseSchedule(objects)
                cont.resume(schedule)
            }
        }
    }

    private fun getListParseSchedule(objects: List<ParseObject>) = objects.map {
        getParseSchedule(it)
    }

    private fun getParseSchedule(obj: ParseObject) = ScheduleObj(
        dayOfWeek = obj.getNumber("dayofweek")?.toInt() ?: 0,
        order = obj.getNumber("order")?.toInt() ?: 0,
        fromDate = obj.getDate("fromdate") ?: Date(),
        toDate = obj.getDate("todate") ?: Date(),
        subjectId = obj.getString("subjectid") ?: ""
    )

    private suspend fun findSubject(name: String): SubjectObj? {
        var subj: SubjectObj? = null
        val query = ParseQuery<ParseObject>(CLASS_SUBJECT)
        query.whereContains("name", name)
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null)
                    objects?.let {
                        if (objects.isNotEmpty()) subj = getParseSubject(objects[0])
                    }
                cont.resume(subj)
            }
        }
    }

    override suspend fun listSchedule(): List<ScheduleObj> {
        if (listOfClasses.isEmpty()) {
            val listSched = getListSchedule()
            listOfClasses = getListScheduleSubject(listSched).toMutableList()
        }
        return listOfClasses
    }

    private suspend fun getListScheduleSubject(sched: List<ScheduleObj>) =
        sched.map {
            if (mapSubjects.isEmpty())
                it.subjectObj = findSubject(it.subjectId ?: "") ?: SubjectObj()
            else
                it.subjectObj = mapSubjects[it.subjectId] ?: SubjectObj()
            it
        }


    override suspend fun listHomework(): List<HomeworkObj> {
        val listHw = getListHomeworkSubject(getListHomework()).toMutableList()
        listHw.forEach {
            it.daysLeft = daysLeftHw(it.subjectId, listOfClasses)
        }
        listHw.sortBy { it.daysLeft }
        return listHw
    }

    private suspend fun getListHomeworkSubject(sched: List<HomeworkObj>) = sched.map {
        if (mapSubjects.isEmpty())
            it.subjectObj = findSubject(it.subjectId ?: "") ?: SubjectObj()
        else
            it.subjectObj = mapSubjects[it.subjectId] ?: SubjectObj()
        it
    }

    private suspend fun getListHomework(): List<HomeworkObj> {
        var homeworks: List<HomeworkObj> = listOf()
        val query = ParseQuery.getQuery<ParseObject>(CLASS_HOMEWORK)
        query.orderByDescending("daysleft")
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null) homeworks = getListParseHomework(objects)
                cont.resume(homeworks)
            }
        }
    }

    private fun getListParseHomework(objects: List<ParseObject>) = objects.map {
        getParseHomework(it)
    }

    private fun getParseHomework(obj: ParseObject) = HomeworkObj(
        subjectId = obj.getString("subjectid") ?: "",
        task = obj.getString("task") ?: "",
        daysLeft = obj.getNumber("daysleft")?.toInt() ?: 0
    )

    override suspend fun listExam(): List<ExamObj> {
        if (listOfExams.isEmpty()) {
            val listExam = getListExam()
            listOfExams = getListExamSubject(listExam).toMutableList()
            listOfExams.forEach {
                it.daysLeft = daysLeft(it.dateExam)
            }
            listOfExams.sortBy { it.daysLeft }
        }
        return listOfExams
    }

    private suspend fun getListExamSubject(exam: List<ExamObj>) = exam.map {
        if (mapSubjects.isEmpty())
            it.subjectObj = findSubject(it.subjectId ?: "") ?: SubjectObj()
        else
            it.subjectObj = mapSubjects[it.subjectId] ?: SubjectObj()
        it
    }

    private suspend fun getListExam(): List<ExamObj> {
        var exams: List<ExamObj> = listOf()
        val query = ParseQuery.getQuery<ParseObject>(CLASS_EXAM)
        query.orderByDescending("daysleft")
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null) exams = getListParseExam(objects)
                cont.resume(exams)
            }
        }
    }

    private fun getListParseExam(objects: List<ParseObject>) = objects.map {
        getParseExam(it)
    }

    private fun getParseExam(obj: ParseObject) = ExamObj(
        subjectId = obj.getString("subjectid") ?: "",
        dateExam = obj.getDate("date") ?: Date(),
        daysLeft = obj.getNumber("daysleft")?.toInt() ?: 0
    )


    companion object {
        private const val CLASS_SUBJECT = "Subject"
        private const val CLASS_SCHEDULE = "Schedule"
        private const val CLASS_HOMEWORK = "Homework"
        private const val CLASS_EXAM = "Exam"
        private const val TIMER_DELAY = 60000L

    }
}