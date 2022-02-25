package ru.geekbrains.android2.classesapp.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.geekbrains.android2.classesapp.databinding.FragmentClassesBinding
import ru.geekbrains.android2.classesapp.placeholder.PlaceholderContent

class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterClasses: ClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ClassesViewModel::class.java)

        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.listClasses.layoutManager = LinearLayoutManager(context)
        adapterClasses = ClassesAdapter(PlaceholderContent.ITEMS)
        binding.listClasses.adapter = adapterClasses
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}