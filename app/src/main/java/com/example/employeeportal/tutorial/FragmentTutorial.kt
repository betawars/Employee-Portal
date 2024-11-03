package com.example.employeeportal.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.example.employeeportal.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FragmentTutorial: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.tutorial_page, true)
            .build()
        val closeTutorialButton:ImageView = view.findViewById(R.id.close_tutorial)

        if(sharedPreferences.getBoolean("is_tutorial_completed?",false)){
            closeTutorialButton.visibility = View.VISIBLE
            val directions = FragmentTutorialDirections.navigateToLanding()
            findNavController().navigate(directions,navOptions)
        }

        val viewPager = view.findViewById<ViewPager2>(R.id.tutorial_view_pager)
        val adapter = TutorialAdapter(childFragmentManager,lifecycle)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById(R.id.dot_indicator_tab_layout) as TabLayout
//        tabLayout.setupWithViewPager2(viewPager, true)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        }.attach()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }


}