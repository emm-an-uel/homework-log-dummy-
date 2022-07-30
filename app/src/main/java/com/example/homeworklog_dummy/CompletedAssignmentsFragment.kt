package com.example.homeworklog_dummy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.homeworklog_dummy.databinding.FragmentCompletedAssignmentsBinding
import java.io.File
import java.io.StringReader

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedAssignmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedAssignmentsFragment : Fragment() {

    private fun deleteAssignment(deletedAssignment: Assignment, sortedAssignmentsList: List<Assignment>) {

        // transfer every assignment from sortedAssignmentsList into array "assignmentsList"
        val assignmentsList = arrayListOf<Assignment>()
        for (assignment in sortedAssignmentsList) {
            assignmentsList.add(assignment)
        }

        // find deletedAssignment by id and remove
        for (assignment in assignmentsList) {
            if (deletedAssignment.id == assignment.id) {
                assignmentsList.remove(assignment)
                break
            }
        }

        // save locally
        val updatedFile = Klaxon().toJsonString(assignmentsList)
        context!!.openFileOutput("fileAssignment", Context.MODE_PRIVATE).use {
            it.write(updatedFile.toByteArray())
        }

        // refresh fragment
        findNavController().navigate(CompletedAssignmentsFragmentDirections.actionCompletedAssignmentsFragmentToRefreshCompletedFragment())
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
                    listAssignments.add(assignment!!) // add all assignments - filter later
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
            if (assignment.status) { // if status == true (ie done)
                val subject = assignment.subject
                val task = assignment.task
                val dueDate = assignment.dueDate

                // display subject, task, dueDate in new table row & add btnDelete
                val tableRow = TableRow(context)
                val tvSubject = TextView(context)
                val tvTask = TextView(context)
                val tvDueDate = TextView(context)

                tvSubject.layoutParams = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
                )
                tvTask.layoutParams = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
                )
                tvDueDate.layoutParams = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
                )

                tvSubject.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tvTask.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tvDueDate.textAlignment = View.TEXT_ALIGNMENT_CENTER

                tvSubject.text = subject
                tvTask.text = task
                tvDueDate.text = dueDate

                tableRow.addView(tvSubject)
                tableRow.addView(tvTask)
                tableRow.addView(tvDueDate)

                val btnDelete = Button(context)
                btnDelete.text = "delete"
                btnDelete.layoutParams = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
                )
                tableRow.addView(btnDelete)
                btnDelete.setOnClickListener {
                    deleteAssignment(assignment, sortedAssignmentsList)
                }

                // add tableRow into tbAssignments
                binding.tbCompletedAssignments.addView(tableRow)
            }

            n++
        }
    }

    private var _binding: FragmentCompletedAssignmentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCompletedAssignmentsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // * display assignments *
        // determine number of existing files
        val files : Array<String> = context!!.fileList()
        val numFiles = files.size

        if (numFiles > 1) { // first file is rList which does not have items as required by displayAssignments
            sortAssignments()
        }

        // button to navigate to main log
        binding.btnNewAssignments.setOnClickListener() {
            findNavController().navigate(CompletedAssignmentsFragmentDirections.actionCompletedAssignmentsFragmentToLogFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}