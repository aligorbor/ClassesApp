package ru.geekbrains.android2.classesapp.ui.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.FragmentSubjectsBinding
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj
import ru.geekbrains.android2.classesapp.utils.showSnackBar

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SubjectsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubjectsViewModel::class.java).apply {
            getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
            getListOfSubjects()
        }
    }

    private fun renderData(appState: AppStateSubjects) = with(binding) {
        when (appState) {
            is AppStateSubjects.Success -> {
                subjectsFragmentLoadingLayout.visibility = View.GONE
                subjectsFragmentRootView.visibility = View.VISIBLE
                initSubjectsList(appState.subjects)
            }
            is AppStateSubjects.Loading -> {
                subjectsFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppStateSubjects.Error -> {
                binding.root.showSnackBar(
                    appState.error.message ?: "",
                    getString(R.string.reload),
                    {
                        viewModel.getListOfSubjects()
                    })
            }
        }
    }

    private fun initSubjectsList(list: List<SubjectObj>) {
        val adapter = SubjectsAdapter(list)

        binding.listSubjects.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}