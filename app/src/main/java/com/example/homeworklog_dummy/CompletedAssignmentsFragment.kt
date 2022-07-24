package com.example.homeworklog_dummy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedAssignmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedAssignmentsFragment : Fragment() {

    private fun displayAssignments() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_assignments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // * populate table view with completed assignments *
        displayAssignments()
    }
}