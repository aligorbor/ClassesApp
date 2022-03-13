package ru.geekbrains.android2.classesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.FragmentHomeBinding
import ru.geekbrains.android2.classesapp.model.entities.ExamObj
import ru.geekbrains.android2.classesapp.model.entities.HomeworkObj
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.utils.showSnackBar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java).apply {
            getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
            getListOfSubjects()
        }
    }

    private fun renderData(appState: AppStateHome) = with(binding) {
        when (appState) {
            is AppStateHome.SuccessSubjects -> {
                viewModel.getListOfExams()
            }
            is AppStateHome.SuccessClasses -> {
                viewModel.getListOfHomeworks()
                initClassesList(
                    appState.classes,
                    appState.numberClasses,
                    appState.scrollToPos,
                    appState.isToday
                )
            }
            is AppStateHome.SuccessHomeworks -> {
                homeFragmentLoadingLayout.visibility = View.GONE
                homeFragmentRootView.visibility = View.VISIBLE
                initHomeworksList(appState.homeworks)
            }
            is AppStateHome.SuccessExams -> {
                viewModel.getListOfClasses()
                initExamsList(appState.exams)
                viewModel.startTicker()
            }
            is AppStateHome.SuccessExamsTimer -> {
                (binding.listExams.adapter as ExamsAdapter).updValues()
            }
            is AppStateHome.Loading -> {
                homeFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppStateHome.Error -> {
                binding.root.showSnackBar(
                    appState.error.message ?: "",
                    getString(R.string.reload),
                    {
                        viewModel.getListOfClasses()
                    })
            }
        }
    }

    private fun initClassesList(
        list: List<ScheduleObj>,
        numberClasses: Int,
        scrollToPos: Int,
        isToday: Boolean
    ) {
        val adapter = ClassesHomeAdapter(list)
        binding.listClassesHome.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        itemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.separator,
                null
            )!!
        )
        binding.listClassesHome.addItemDecoration(itemDecoration)
        (binding.listClassesHome.adapter as ClassesHomeAdapter).setPosition(scrollToPos)
        binding.listClassesHome.scrollToPosition(scrollToPos)
        binding.textClassesNumber.text = String.format(
            getString(if (isToday) R.string.classes_today else R.string.classes_tomorrow),
            numberClasses
        )
    }

    private fun initHomeworksList(list: List<HomeworkObj>) {
        val adapter = HomeworkAdapter(list)
        binding.listHomeWork.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        itemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.separator,
                null
            )!!
        )
        binding.listHomeWork.addItemDecoration(itemDecoration)
    }

    private fun initExamsList(list: List<ExamObj>) {
        val adapter = ExamsAdapter(list)
        binding.listExams.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        itemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.separator,
                null
            )!!
        )
        binding.listExams.addItemDecoration(itemDecoration)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}