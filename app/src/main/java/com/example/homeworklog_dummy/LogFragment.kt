package com.example.homeworklog_dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // display new assignments
        if (args.newAssignment == true) {
            val subject = args.subject
            val task = args.task
            val dueDate = args.dueDate
            val notes = args.notes
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