package com.example.employeeportal.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.employeeportal.tutorial.fragments.TutorialFragment1
import com.example.employeeportal.tutorial.fragments.TutorialFragment2
import com.example.employeeportal.tutorial.fragments.TutorialFragment3
import com.example.employeeportal.tutorial.fragments.TutorialFragment4

class TutorialAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TutorialFragment1()
            1 -> TutorialFragment2()
            2 -> TutorialFragment3()
            3 -> TutorialFragment4()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}