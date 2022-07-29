package com.example.homeworklog_dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.Klaxon
import com.beust.klaxon.JsonReader
import com.example.homeworklog_dummy.databinding.FragmentLogBinding
import java.io.File
import java.io.StringReader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LogFragment : Fragment() {

    private fun markAsDone(subTaskDate: String) {
        // run through existing files to find the one with content
        // "subTaskDate" and delete that file
        // File(fileName).readLines()

        val files : Array<String> = context!!.fileList()
        val numFiles = files.size
        var n = 1
        while (n < numFiles) {
            val fileName = "file$n"
            var fileContents = File(context!!.filesDir, fileName).readText()
            fileContents = fileContents.dropLast(9)

            if (fileContents == subTaskDate) {
                context!!.deleteFile(fileName)
                findNavController().navigate(LogFragmentDirections.actionLogFragmentToStartFragment()) // refresh
                break
            }

            n ++
        }
    }

    private fun sortAssignments(): List<Assignment> {

        // read json file
        val listAssignments = arrayListOf<Assignment>()
        val fileJson = File(context!!.filesDir, "fileAssignment").readText()

        // add undone assignments from fileJson to listAssignments
        JsonReader(StringReader(fileJson)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val assignment = Klaxon().parse<Assignment>(reader)

                    if (!assignment!!.status) { // if assignment is undone
                        listAssignments.add(assignment)
                    }
                }
            }
        }

        // sort by dueDateInt and return
        return listAssignments.sortedBy { it.dateInt }
    }

    private fun displayAssignments(sortedAssignmentsList: List<Assignment>) {
        var n = 0
        while (n < sortedAssignmentsList.size) {
            val assignment = sortedAssignmentsList[n]
            val subject = assignment.subject
            val task = assignment.task
            val dueDate = assignment.dueDate

            // display subject, task, dueDate in new table row & add btnDone
            val tableRow = TableRow(context)
            val tvSubject = TextView(context)
            val tvTask = TextView(context)
            val tvDueDate = TextView(context)

            tvSubject.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1f)
            tvTask.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1f)
            tvDueDate.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1f)

            tvSubject.text = subject
            tvTask.text = task
            tvDueDate.text = dueDate

            tableRow.addView(tvSubject)
            tableRow.addView(tvTask)
            tableRow.addView(tvDueDate)

            val btnDone = Button(context)
            // TODO: ADD btnDone AND ITS FUNCTIONALITIES 

            // add tableRow into tbAssignments
            binding.tbAssignments.addView(tableRow)

            n++
        }
    }

    private var _binding: FragmentLogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // * display assignments *
        // determine number of existing files
        val files : Array<String> = context!!.fileList()
        val numFiles = files.size

        if (numFiles > 1) { // first file is rList which does not have items as required by displayAssignments
            
            val sortedAssignmentsList = sortAssignments() // returns sorted list
            displayAssignments(sortedAssignmentsList) // pass a sorted list to the function, function displays each item in fragment_log
        }

        // button to create new assignment
        binding.newAssignment.setOnClickListener {
            findNavController().navigate(LogFragmentDirections.actionLogFragmentToInputFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}