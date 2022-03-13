package ru.geekbrains.android2.classesapp.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.FragmentClassesBinding
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.utils.showSnackBar

class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ClassesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClassesViewModel::class.java).apply {
            getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
            getListOfSubjects()
        }

    }

    private fun renderData(appState: AppStateClasses) = with(binding) {
        when (appState) {
            is AppStateClasses.SuccessSubjects -> {
                viewModel.getListOfClasses()
            }
            is AppStateClasses.Success -> {
                classesFragmentLoadingLayout.visibility = View.GONE
                classesFragmentRootView.visibility = View.VISIBLE
                initClassesList(appState.classes, appState.scrollToPos)
            }
            is AppStateClasses.Loading -> {
                classesFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppStateClasses.Error -> {
                binding.root.showSnackBar(
                    appState.error.message ?: "",
                    getString(R.string.reload),
                    {
                        viewModel.getListOfClasses()
                    })
            }
        }
    }

    private fun initClassesList(list: List<ScheduleObj>, scrollToPos: Int) {
        val adapter = ClassesAdapter(list)
        binding.listClasses.adapter = adapter
        (binding.listClasses.adapter as ClassesAdapter).setPosition(scrollToPos)
        binding.listClasses.scrollToPosition(scrollToPos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

