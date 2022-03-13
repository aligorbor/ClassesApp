package ru.geekbrains.android2.classesapp.ui.classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.geekbrains.android2.classesapp.model.Repository
import ru.geekbrains.android2.classesapp.model.RepositoryImpl
import ru.geekbrains.android2.classesapp.utils.currentClassInd

class ClassesViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateClasses> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) : ViewModel(), CoroutineScope by MainScope() {

    fun getLiveData() = liveDataToObserve

    fun getListOfSubjects() {
        liveDataToObserve.value = AppStateClasses.Loading
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateClasses.SuccessSubjects(repository.listSubject())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateClasses.Error(e))
            }
        }
    }

    fun getListOfClasses() {
        launch(Dispatchers.IO) {
            try {
                val list = repository.listSchedule()
                liveDataToObserve.postValue(
                    AppStateClasses.Success(list, currentClassInd(list))
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateClasses.Error(e))
            }
        }
    }
}