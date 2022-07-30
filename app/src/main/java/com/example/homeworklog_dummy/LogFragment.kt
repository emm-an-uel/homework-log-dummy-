package com.example.homeworklog_dummy

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.homeworklog_dummy.databinding.FragmentLogBinding
import java.io.File
import java.io.StringReader


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LogFragment : Fragment() {

    private fun storeJson(completedAssignment: Assignment, sortedAssignmentsList: List<Assignment>) {

        // transfer every assignment from sortedAssignmentsList into array "assignmentsList"
        val assignmentsList = arrayListOf<Assignment>()
        for (assignment in sortedAssignmentsList) {
            assignmentsList.add(assignment)
        }

        // find completedAssignment by id
        for (assignment in assignmentsList) {
            if (completedAssignment.id == assignment.id) {
                assignmentsList.remove(assignment)
                break
            }
        }

        // change to reflect "done"
        completedAssignment.status = true
        assignmentsList.add(completedAssignment)

        // save locally
        val updatedFile = Klaxon().toJsonString(assignmentsList)
        context!!.openFileOutput("fileAssignment", Context.MODE_PRIVATE).use {
            it.write(updatedFile.toByteArray())
        }
    }

    private fun markAsDone(assignment: Assignment, sortedAssignmentsList: List<Assignment>) {

        storeJson(assignment, sortedAssignmentsList)

        // refresh fragment
        findNavController().navigate(LogFragmentDirections.actionLogFragmentToStartFragment())
    }

    private fun sortAssignments() {

        // read json file
        val listAssignments = arrayListOf<Assignment>()
        val fileJson = File(context!!.filesDir, "fileAssignment").readText()

        // add undone assignments from fileJson to listAssignments
        JsonReader(StringReader(fileJson)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val assignment = Klaxon().parse<Assignment>(reader)

                    if (!assignment!!.status) { // if status == false (assignment is undone)
                        listAssignments.add(assignment)
                    }
                }
            }
        }

        // sort by dueDateInt and return
        displayAssignments(listAssignments.sortedBy { it.dateInt })
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

            tvSubject.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvTask.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvDueDate.textAlignment = View.TEXT_ALIGNMENT_CENTER

            tvSubject.text = subject
            tvTask.text = task
            tvDueDate.text = dueDate

            tableRow.addView(tvSubject)
            tableRow.addView(tvTask)
            tableRow.addView(tvDueDate)

            val btnDone = Button(context)
            btnDone.text = "done"
            btnDone.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1f)
            tableRow.addView(btnDone)
            btnDone.setOnClickListener {
                markAsDone(assignment, sortedAssignmentsList)
            }

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

        // button to create new assignment
        binding.newAssignment.setOnClickListener {
            findNavController().navigate(LogFragmentDirections.actionLogFragmentToInputFragment())
        }

        // button to navigate to completed log
        binding.btnCompletedAssignments.setOnClickListener {
            findNavController().navigate(LogFragmentDirections.actionLogFragmentToCompletedAssignmentsFragment())
        }

        // * display assignments *
        // determine number of existing files
        val files : Array<String> = context!!.fileList()
        val numFiles = files.size

        if (numFiles > 1) { // first file is rList which does not have items as required by displayAssignments
            sortAssignments()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}