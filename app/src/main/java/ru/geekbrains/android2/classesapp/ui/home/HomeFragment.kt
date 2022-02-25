package ru.geekbrains.android2.classesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.FragmentHomeBinding
import ru.geekbrains.android2.classesapp.placeholder.PlaceholderContent
import ru.geekbrains.android2.classesapp.ui.classes.ClassesAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterClassesHome: ClassesHomeAdapter
    private lateinit var adapterExams: ExamsAdapter
    private lateinit var adapterHomework: HomeworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

     //   val textView: TextView = binding.textClasses
        homeViewModel.text.observe(viewLifecycleOwner) {
            binding.textClasses.text = it
            binding.textHomework.text = it
        }
        adapterClassesHome = ClassesHomeAdapter(PlaceholderContent.ITEMS)
        binding.listClassesHome.adapter = adapterClassesHome
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.separator,null))
        binding.listClassesHome.addItemDecoration(itemDecoration)
        adapterExams = ExamsAdapter(PlaceholderContent.ITEMS)
        binding.listExams.adapter = adapterExams
        binding.listExams.addItemDecoration(itemDecoration)
        adapterHomework = HomeworkAdapter(PlaceholderContent.ITEMS)
        binding.listHomeWork.adapter = adapterHomework
        binding.listHomeWork.addItemDecoration(itemDecoration)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}