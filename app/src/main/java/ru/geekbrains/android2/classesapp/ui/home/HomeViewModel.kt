package ru.geekbrains.android2.classesapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.geekbrains.android2.classesapp.model.Repository
import ru.geekbrains.android2.classesapp.model.RepositoryImpl
import ru.geekbrains.android2.classesapp.utils.currentClassInd
import ru.geekbrains.android2.classesapp.utils.currentDayOfWeek

class HomeViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateHome> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) : ViewModel(), CoroutineScope by MainScope() {
    fun getLiveData() = liveDataToObserve

    fun getListOfSubjects() {
        liveDataToObserve.value = AppStateHome.Loading
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateHome.SuccessSubjects(repository.listSubject())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateHome.Error(e))
            }
        }
    }

    fun getListOfClasses() {
        launch(Dispatchers.IO) {
            try {
                val dToday = currentDayOfWeek()
                val list = repository.listSchedule()
                val ind = currentClassInd(list)
                val d = list[ind].dayOfWeek
                val ordr = list[ind].order
                val listFiltered = list.filter { it.dayOfWeek == d }
                val pos = listFiltered.indexOf(listFiltered.find { it.order == ordr })
                liveDataToObserve.postValue(
                    AppStateHome.SuccessClasses(
                        listFiltered,
                        listFiltered.count(),
                        pos,
                        d == dToday
                    )
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateHome.Error(e))
            }
        }
    }

    fun getListOfHomeworks() {
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateHome.SuccessHomeworks(repository.listHomework())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateHome.Error(e))
            }
        }
    }

    fun getListOfExams() {
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateHome.SuccessExams(repository.listExam())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateHome.Error(e))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopTicker()
    }

    fun startTicker() = repository.startTicker()


    init {
        viewModelScope.launch {
            repository.ticker.collect {
                liveDataToObserve.value = AppStateHome.SuccessExamsTimer
            }
        }
    }
}