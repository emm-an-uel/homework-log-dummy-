package com.example.homeworklog_dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homeworklog_dummy.databinding.FragmentLogBinding
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LogFragment : Fragment() {

    private fun displayAssignments(numFiles: Int) {
        // * access data from local file *

        // display each file in a new row
        var n = 1 // first file is "rList" so file naming starts with "file1"
        while (n < numFiles) {
            val file = File(context!!.filesDir, "file$n").readText() // eg access file1

            // convert "file" from string to list
            val list : List<String> = file.split("-").toList()

            // assign variable to each item in list
            val subject = list[0]
            val task = list[1]
            val dueDate = list[2]
            val dueDateInt = list[3]

            // create new horizontal linear layout
            val linearLayoutHorizontal = LinearLayout(context)
            linearLayoutHorizontal.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                0,
                3F // weight 3 to fit 3 text views
            )
            linearLayoutHorizontal.orientation = LinearLayout.HORIZONTAL // horizontal layout
            binding.linearLayout.addView(linearLayoutHorizontal) // add horizontal layout to parent vertical layout

            // create new text views
            val textViewSubject = TextView(context)
            val textViewTask = TextView(context)
            val textViewDueDate = TextView(context)

            // populate text views with local data retrieved above
            textViewSubject.text = subject
            textViewSubject.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewSubject.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT,
                1F // weight is set to 1F so all 3 text views are evenly distributed to take up the whole row
            )

            textViewTask.text = task
            textViewTask.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewTask.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT,
                1F
            )

            textViewDueDate.text = dueDate
            textViewDueDate.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewDueDate.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT,
                1F
            )

            // display 3 columns of info
            linearLayoutHorizontal.addView(textViewSubject)
            linearLayoutHorizontal.addView(textViewTask)
            linearLayoutHorizontal.addView(textViewDueDate)

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
            displayAssignments(numFiles)
        }

        // button to create new assignment
        binding.newAssignment.setOnClickListener() {
            findNavController().navigate(LogFragmentDirections.actionLogFragmentToInputFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}