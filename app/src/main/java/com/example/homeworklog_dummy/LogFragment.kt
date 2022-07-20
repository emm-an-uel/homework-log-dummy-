package com.example.homeworklog_dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            // create new text views
            val textViewSubject = TextView(context)
            val textViewTask = TextView(context)
            val textViewDueDate = TextView(context)

            // populate text views with data from fragment_input
            textViewSubject.text = subject
            textViewSubject.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            textViewTask.text = task
            textViewTask.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewDueDate.text = dueDate
            textViewDueDate.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            // new horizontal linear layout to display subject, task, due date
            val linearLayoutHorizontal = LinearLayout(context)
            linearLayoutHorizontal.orientation = LinearLayout.HORIZONTAL

            binding.linearLayout.addView(linearLayoutHorizontal)

            // display 3 columns of info
            linearLayoutHorizontal.addView(textViewSubject)
            linearLayoutHorizontal.addView(textViewTask)
            linearLayoutHorizontal.addView(textViewDueDate)
        }

        // create new assignment
        binding.newAssignment.setOnClickListener() {
            findNavController().navigate(LogFragmentDirections.actionLogFragmentToInputFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}