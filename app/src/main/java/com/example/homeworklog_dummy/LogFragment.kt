package com.example.homeworklog_dummy

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homeworklog_dummy.databinding.FragmentLogBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LogFragment : Fragment() {

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

    val args: LogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // display assignments
        if (args.newAssignment) {
            // retrieve data from fragment_input
            val subject = args.subject
            val task = args.task
            val dueDate = args.dueDate
            val notes = args.notes

            // create new horizontal linear layout
            val linearLayoutHoriz1 = LinearLayout(context)
            linearLayoutHoriz1.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                0,
                3F // weight 3 to fit 3 text views
            )
            linearLayoutHoriz1.orientation = LinearLayout.HORIZONTAL // horizontal layout
            binding.linearLayout.addView(linearLayoutHoriz1) // add horizontal layout to parent vertical layout

            // create new text views
            val textViewSubject = TextView(context)
            val textViewTask = TextView(context)
            val textViewDueDate = TextView(context)

            // populate text views with data from fragment_input
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
            linearLayoutHoriz1.addView(textViewSubject)
            linearLayoutHoriz1.addView(textViewTask)
            linearLayoutHoriz1.addView(textViewDueDate)
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