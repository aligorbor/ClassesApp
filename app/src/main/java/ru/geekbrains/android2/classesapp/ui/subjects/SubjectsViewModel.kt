package ru.geekbrains.android2.classesapp.ui.subjects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.geekbrains.android2.classesapp.model.Repository
import ru.geekbrains.android2.classesapp.model.RepositoryImpl

class SubjectsViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateSubjects> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) : ViewModel(), CoroutineScope by MainScope() {

    fun getLiveData() = liveDataToObserve

    fun getListOfSubjects() {
        liveDataToObserve.value = AppStateSubjects.Loading
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateSubjects.Success(repository.listSubject())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateSubjects.Error(e))
            }
        }
    }
}