package ru.geekbrains.android2.classesapp.model.entities

data class SubjectObj(
    var name: String = "",
    var teacher: String = "",
    var about: String = "",
    var obligatory: Boolean = true,
    var imageId: Int = 0,
    var openIn: Boolean = false
)
