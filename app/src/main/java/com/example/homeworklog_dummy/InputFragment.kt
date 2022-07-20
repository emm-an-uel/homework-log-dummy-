package com.example.homeworklog_dummy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homeworklog_dummy.databinding.FragmentInputBinding
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dueDate = ""

        // datePicker stuff
        val today = Calendar.getInstance()
        binding.dueDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
                val month = month + 1
                dueDate = "$day $month $year"
            }

        // when button "confirm" is clicked
        binding.buttonConfirm.setOnClickListener() {

            val subject = binding.subject.text.toString()
            val task = binding.task.text.toString()
            val notes = binding.notes.text.toString()
            val newAssignment = true

            // sends subject, task, dueDate, notes to LogFragment and navigates there
            val action = InputFragmentDirections.actionInputFragmentToLogFragment(subject, task, dueDate, notes, newAssignment)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}