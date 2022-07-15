package com.example.homeworklog_dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.homeworklog_dummy.databinding.FragmentInputBinding
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.widget.EditText
import android.widget.Toast
import java.time.format.DateTimeFormatter

import java.util.Calendar

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

        // declare variables (user input)
        val subject = binding.subject.text
        val task = binding.task.text
        val notes = binding.notes.text

        // datePicker stuff
        val today = Calendar.getInstance()
        binding.dueDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
                val month = month + 1
                val msg = "You Selected: $day/$month/$year"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}