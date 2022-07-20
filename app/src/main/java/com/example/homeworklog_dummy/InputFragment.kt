package com.example.homeworklog_dummy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homeworklog_dummy.databinding.FragmentInputBinding
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputFragment : Fragment() {

    private fun storeLocally(subject : String, task : String, dueDate : String) {

        // merge into string
        val contentToFile = "$subject-$task-$dueDate"

        // * store contentToFile into local file *

        // determine number of existing files
        val files : Array<String> = context!!.fileList()
        val numFiles = files.size

        // create new file
        val fileName = "file$numFiles" // eg if there's no files, name is file0.
        // if 1 file, name is file1
        val fileContents = contentToFile
        context!!.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

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

            // stores subject, task, notes in local file
            storeLocally(subject, task, dueDate)

            // navigate to fragment_log
            findNavController().navigate(InputFragmentDirections.actionInputFragmentToLogFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}