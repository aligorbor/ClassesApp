package ru.geekbrains.android2.classesapp.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import java.time.Duration
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun DHMFromNow(date: Date): String {
    val today = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val duration = Duration.between(today, localDate)
    var strD = duration.toDays().toString()
    var strH = duration.minusDays(duration.toDays()).toHours().toString()
    var strM = duration.minusHours(duration.toHours()).toMinutes().toString()
    if (strD.length < 2) strD = "0$strD"
    if (strD.length > 2) strD = "99"
    if (strH.length < 2) strH = "0$strH"
    if (strM.length < 2) strM = "0$strM"
    return "$strD:$strH:$strM"
}

fun dateToStr(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/y HH:mm"))
}

fun dateToStrHM(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDate.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun dateToStrDayOfWeek(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDate.dayOfWeek.name
}

fun daysLeft(date: Date): Int {
    val today = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val duration = Duration.between(today, localDate)
    return duration.toDays().toInt()
}

fun currentDayOfWeek(): Int {
    val calendar = Calendar.getInstance()
    val d = calendar.get(Calendar.DAY_OF_WEEK)
    return if (d > 1) d - 1 else 7
}

fun daysLeftHw(subjectId: String, listSched: List<ScheduleObj>): Int {
    var dayslft = 0
    val currDay = currentDayOfWeek()
    var lstSchd = listSched.toMutableList()
    lstSchd = lstSchd.filter {
        it.subjectObj.name == subjectId && it.dayOfWeek > currDay
    }.toMutableList()
    if (lstSchd.isEmpty()) {
        lstSchd = listSched.toMutableList()
        lstSchd = lstSchd.filter {
            it.subjectObj.name == subjectId && it.dayOfWeek <= currDay
        }.toMutableList()
        if (!lstSchd.isEmpty()) dayslft = 7 - currDay + lstSchd[0].dayOfWeek
    } else {
        dayslft = lstSchd[0].dayOfWeek - currDay
    }
    return dayslft
}

fun currentClassInd(listSched: List<ScheduleObj>): Int {
    var ordr = 1
    var dayOfW = currentDayOfWeek()
    val today = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

    var lstSchd = listSched.toMutableList()
    lstSchd = lstSchd.filter {
        val classDay = it.toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        it.dayOfWeek == dayOfW && (today.hour < classDay.hour || (today.hour == classDay.hour && today.minute <= classDay.minute))
    }.toMutableList()
    if (lstSchd.isEmpty()) {
        dayOfW++
        if (dayOfW > 7) dayOfW = 1
    } else {
        ordr = lstSchd.last().order
    }
    val obj = listSched.find { it.dayOfWeek == dayOfW && it.order == ordr }
    return if (obj == null) 0 else listSched.indexOf(obj)
}

fun startSkype(context: Context): Boolean {
    try {
        val sky = Intent("android.intent.action.VIEW")
        sky.data = Uri.parse("skype:<skype_id>")
        startActivity(context, sky, null)
        return true
    } catch (e: ActivityNotFoundException) {
        return false
    }
}
