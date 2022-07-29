package com.example.homeworklog_dummy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.Klaxon
import com.beust.klaxon.JsonReader
import com.example.homeworklog_dummy.databinding.FragmentInputBinding
import java.io.File
import java.io.StringReader
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputFragment : Fragment() {

    private fun createDateInt(day: Int, month: Int, year: Int): Int {
        // * dueDateSort will be in format YYYYMMDD for easy sorting of due dates *

        var monthString = month.toString()
        var dayString = day.toString()

        // ensure proper MM format
        if (month < 10) {
            monthString = "0$month" // eg convert "8" to "08"
        }

        // ensure proper DD format
        if (day < 10) {
            dayString = "0$day"
        }

        // convert to YYYYMMDD format
        val dateString = "$year$monthString$dayString"
        val dateInt = dateString.toInt() // return integer so it can be sorted

        return(dateInt)
    }

    private fun storeLocally(subject : String, task : String, dueDate : String, dateInt: Int, status: Boolean) {

        // create val "assignment" using Class "Assignment" parameters
        val newAssignment = Assignment(subject, task, dueDate, dateInt, status)

        // check if there's existing "fileAssignments"
        var fileExists = false
        val files = context!!.fileList()
        for (file in files) {
            if (file == "fileAssignment") {
                fileExists = true
                break
            }
        }

        if (fileExists) { // if there's existing "fileAssignments"

            val listAssignments = arrayListOf<Assignment>()

            // read json
            val fileJson = File(context!!.filesDir, "fileAssignment").readText()

            // add all items in fileJson into listAssignments
            JsonReader(StringReader(fileJson)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val assignment = Klaxon().parse<Assignment>(reader)
                        listAssignments.add(assignment!!)
                    }
                }
            }

            // add newAssignment and serialize listAssignments
            listAssignments.add(newAssignment)
            val updatedFile = Klaxon().toJsonString(listAssignments)

            // store in local file
            context!!.openFileOutput("fileAssignment", Context.MODE_PRIVATE).use {
                it.write(updatedFile.toByteArray())
            }

        } else { // if "fileAssignment" does not exist

            // new val listAssignment, add newAssignment and serialize listAssignments
            val listAssignment = mutableListOf<Assignment>(newAssignment)
            val updatedFile = Klaxon().toJsonString(listAssignment)

            // store in local file
            context!!.openFileOutput("fileAssignment", Context.MODE_PRIVATE).use {
                it.write(updatedFile.toByteArray())
            }
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
        var dateInt = 0

        // datePicker stuff
        val today = Calendar.getInstance()
        binding.dueDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
                val month = month + 1
                dueDate = "$day $month $year"

                // sort by due date
                dateInt = createDateInt(day, month, year)
            }

        // when button "confirm" is clicked
        binding.buttonConfirm.setOnClickListener() {

            val subject = binding.subject.text.toString()
            val task = binding.task.text.toString()
            val status = false // false = undone, true = done

            // stores subject, task, notes in local file
            storeLocally(subject, task, dueDate, dateInt, status)

            // navigate to fragment_log
            findNavController().navigate(InputFragmentDirections.actionInputFragmentToLogFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}