package com.example.employeeportal.tutorial.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.employeeportal.R

class TutorialFragment4: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val directToLanding: Button = view.findViewById(R.id.direct_to_landing)

        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.tutorial_page, true)
            .build()

        directToLanding.setOnClickListener {
            val directions = TutorialFragment3Directions.navigateToLanding()
            findNavController().navigate(directions,navOptions)
        }

    }
}