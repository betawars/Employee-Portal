package com.example.employeeportal.landingPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.employeeportal.R


class FragmentLanding: Fragment(R.layout.fragment_landing) {
    private val editTextTrack = mutableListOf<Int>()
    private val buttonTrack = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_landing, container, false)
        val addWorkplaceButton : Button = rootView.findViewById(R.id.add_work_place_button)
        val addWorkPlaceEditText:EditText = rootView.findViewById(R.id.add_work_pace_edit_text)
        val containerLayout = rootView.findViewById<ConstraintLayout>(R.id.fragment_landing)
        addWorkplaceButton.setOnClickListener {

            val addNewWorkplaceEditText = EditText(requireContext())
            val clearNewWorkPlaceEditTextButton = ImageView(requireContext())
            clearNewWorkPlaceEditTextButton.id = View.generateViewId()
            addNewWorkplaceEditText.id = View.generateViewId()
            clearNewWorkPlaceEditTextButton.setImageResource(R.drawable.ic_close_edit_text)

            containerLayout.addView(addNewWorkplaceEditText)
            containerLayout.addView(clearNewWorkPlaceEditTextButton)
            val paramsAddWorkPlace = addWorkPlaceEditText.layoutParams as LayoutParams
            val paramsAddTextFieldButton = addWorkplaceButton.layoutParams as LayoutParams

            arrangeConstraints(addNewWorkplaceEditText,
                paramsAddWorkPlace,
                clearNewWorkPlaceEditTextButton,
                addWorkPlaceEditText,
                paramsAddTextFieldButton,
                addWorkplaceButton
                )

            clearNewWorkPlaceEditTextButton.setOnClickListener{
                reArrangeConstraints(addNewWorkplaceEditText,
                    paramsAddWorkPlace,
                    clearNewWorkPlaceEditTextButton,
                    addWorkPlaceEditText,
                    paramsAddTextFieldButton,
                    addWorkplaceButton,
                    rootView,
                    containerLayout)
            }
        }
        return rootView
    }

    private fun reArrangeConstraints(addNewWorkplaceEditText:EditText,
                                     paramsAddWorkPlace:LayoutParams,
                                     clearNewWorkPlaceEditTextButton:ImageView,
                                     addWorkPlaceEditText:EditText,
                                     paramsAddTextFieldButton:LayoutParams,
                                     addWorkplaceButton:Button,
                                     rootView: View,
                                     containerLayout: ConstraintLayout){

        val currentIndex = editTextTrack.indexOf(addNewWorkplaceEditText.id)
        var currentAddNewWorkplaceEditText: EditText = rootView.findViewById(editTextTrack.elementAt(0))
        var currentClearButton: ImageView = rootView.findViewById(buttonTrack.elementAt(0))
        if(currentIndex!=editTextTrack.indexOf(editTextTrack.last())){

            currentAddNewWorkplaceEditText =  rootView.findViewById(editTextTrack.elementAt(currentIndex+1))
            currentClearButton = rootView.findViewById(buttonTrack.elementAt(currentIndex+1))

        }


        Log.d("edittextlist",editTextTrack.toString())
        Log.d("buttonlist",buttonTrack.toString())
        Log.d("currentedit text", currentAddNewWorkplaceEditText.id.toString())
        Log.d("currentedit button", currentClearButton.id.toString())

        if (currentIndex == 0){
            currentAddNewWorkplaceEditText.updateLayoutParams<LayoutParams> {
                marginEnd = paramsAddWorkPlace.marginEnd
                marginStart = paramsAddWorkPlace.marginStart-75
                startToStart = paramsAddWorkPlace.startToStart
                topToBottom = addWorkPlaceEditText.id
                endToStart = currentClearButton.id
                width = addWorkPlaceEditText.width
            }
            containerLayout.removeView(rootView.findViewById(editTextTrack.elementAt(currentIndex)))
            editTextTrack.remove(editTextTrack.elementAt(currentIndex))

            currentClearButton.updateLayoutParams<LayoutParams> {
                marginEnd = paramsAddTextFieldButton.marginEnd
                topMargin = 50
                startToEnd = currentAddNewWorkplaceEditText.id
                topToBottom = addWorkplaceButton.id
                endToEnd = LayoutParams.PARENT_ID
            }
            containerLayout.removeView(rootView.findViewById(buttonTrack.elementAt(currentIndex)))
            buttonTrack.remove(buttonTrack.elementAt(currentIndex))

        }else if(editTextTrack.size != 1 && buttonTrack.size != 1
            && addNewWorkplaceEditText.id != editTextTrack.last() && clearNewWorkPlaceEditTextButton.id != buttonTrack.last()
            && addNewWorkplaceEditText.id != editTextTrack.first() && clearNewWorkPlaceEditTextButton.id != buttonTrack.first()){


            currentAddNewWorkplaceEditText.updateLayoutParams<LayoutParams> {
                marginEnd = paramsAddWorkPlace.marginEnd
                marginStart = paramsAddWorkPlace.marginStart-75
                startToStart = paramsAddWorkPlace.startToStart
                topToBottom = editTextTrack.elementAt(currentIndex-1)
                endToStart = currentClearButton.id
                width = addWorkPlaceEditText.width
            }
            containerLayout.removeView(rootView.findViewById(editTextTrack.elementAt(currentIndex)))
            editTextTrack.remove(editTextTrack.elementAt(currentIndex))

            currentClearButton.updateLayoutParams<LayoutParams> {
                marginEnd = paramsAddTextFieldButton.marginEnd
                topMargin = 50
                startToEnd = currentAddNewWorkplaceEditText.id
                topToBottom = buttonTrack.elementAt(currentIndex-1)
                endToEnd = LayoutParams.PARENT_ID
            }
            containerLayout.removeView(rootView.findViewById(buttonTrack.elementAt(currentIndex)))
            buttonTrack.remove(buttonTrack.elementAt(currentIndex))

        }
        else{
            containerLayout.removeView(rootView.findViewById(editTextTrack.elementAt(currentIndex)))
            editTextTrack.remove(editTextTrack.elementAt(currentIndex))
            containerLayout.removeView(rootView.findViewById(buttonTrack.elementAt(currentIndex)))
            buttonTrack.remove(buttonTrack.elementAt(currentIndex))
        }


    }

    private fun arrangeConstraints(addNewWorkplaceEditText:EditText,
                                   paramsAddWorkPlace:LayoutParams,
                                   clearNewWorkPlaceEditTextButton:ImageView,
                                   addWorkPlaceEditText:EditText,
                                   paramsAddTextFieldButton:LayoutParams,
                                   addWorkplaceButton:Button,
                                   ){


        if(editTextTrack.isNotEmpty() && buttonTrack.isNotEmpty()){
            addNewWorkplaceEditText.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginEnd = paramsAddWorkPlace.marginEnd
                marginStart = paramsAddWorkPlace.marginStart-75
                startToStart = paramsAddWorkPlace.startToStart
                topToBottom = editTextTrack.last()
                endToStart = clearNewWorkPlaceEditTextButton.id
                width = addWorkPlaceEditText.width
            }
            editTextTrack.add(addNewWorkplaceEditText.id)
            clearNewWorkPlaceEditTextButton.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginEnd = paramsAddTextFieldButton.marginEnd
                topMargin = 50
                startToEnd = addNewWorkplaceEditText.id
                topToBottom = buttonTrack.last()
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            buttonTrack.add(clearNewWorkPlaceEditTextButton.id)
        }else{
            addNewWorkplaceEditText.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginEnd = paramsAddWorkPlace.marginEnd
                marginStart = paramsAddWorkPlace.marginStart-75
                startToStart = paramsAddWorkPlace.startToStart
                topToBottom = addWorkPlaceEditText.id
                endToStart = clearNewWorkPlaceEditTextButton.id
                width = addWorkPlaceEditText.width
            }
            editTextTrack.add(addNewWorkplaceEditText.id)
            clearNewWorkPlaceEditTextButton.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginEnd = paramsAddTextFieldButton.marginEnd
                topMargin = 50
                startToEnd = addNewWorkplaceEditText.id
                topToBottom = addWorkplaceButton.id
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            buttonTrack.add(clearNewWorkPlaceEditTextButton.id)
        }

    }

}