package com.example.employeeportal.landingPage

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.employeeportal.R
import com.example.employeeportal.encrypter.EncryptionHelper
import com.example.employeeportal.operations.data.OperatorRepo
import com.example.employeeportal.operations.data.OperatorViewModel
import com.google.android.material.snackbar.Snackbar


class ShowTutorialDialog : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Do you want to go back to the tutorial screen?")
                .setPositiveButton("Yes") { dialog, id ->
                    val directions = FragmentLandingDirections.navigateToTutorial()
                    findNavController().navigate(directions)
                }
                .setNegativeButton("No") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class ShowJobNameDialog(private val jobNames:String, private var jobNameList:List<String>) : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Job selector")
            val sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            val container = LinearLayout(requireContext())
            container.orientation = LinearLayout.VERTICAL

            val jobsList = jobNames.split(",")

            val numberOfCheckboxes = jobsList.size
            for (i in 0 until numberOfCheckboxes) {
                val checkBox = CheckBox(requireContext())
                checkBox.text = jobsList[i]
                container.addView(checkBox)
            }



            builder.setView(container)
            builder.setMessage("Please select the jobs you want to log time for")

                .setPositiveButton("Yes") { dialog, id ->
                    jobNameList = jobNames.split(',').toMutableList()
                    editor.putString("fetched_jobs","")
                    editor.apply()
                    dismiss()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class ShowDuoCodeDialog(private val duoCode:String) : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Your duo code $duoCode")
                .setPositiveButton("Ok") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class FragmentLanding: Fragment(R.layout.fragment_landing) {
    private val editTextTrack = mutableListOf<Int>()
    private var jobNameList = mutableListOf<String>()
    private val buttonTrack = mutableListOf<Int>()
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var webView:WebView
    val viewModelO:OperatorViewModel by viewModels()
    var manualEditFlag = false
    var jobNamePopulateMethod = "auto"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        val rootView = inflater.inflate(R.layout.fragment_landing, container, false)
        userNameEditText = rootView.findViewById(R.id.cred_username_edit_text)
        passwordEditText = rootView.findViewById(R.id.cred_password_edit_text)
        val addWorkplaceButton : Button = rootView.findViewById(R.id.add_work_place_button)
        val addWorkPlaceEditText:EditText = rootView.findViewById(R.id.add_work_pace_edit_text)
        val containerLayout = rootView.findViewById<ConstraintLayout>(R.id.fragment_landing)
        val submitFormButton:Button = rootView.findViewById(R.id.submit_form_button)
        val tutorialButton:ImageView = rootView.findViewById(R.id.information_icon_view)
        val autoPopulateButton:Button = rootView.findViewById(R.id.auto_populate_job_name)
        val manualPopulateButton: Button = rootView.findViewById(R.id.manual_populate_job_name)

        webView = rootView.findViewById(R.id.hidden_web_view_landing)
        addWorkplaceButton.visibility = View.INVISIBLE
        addWorkPlaceEditText.visibility= View.INVISIBLE

        if(sharedPrefs.getBoolean("is_user_logged_in?",false)){
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.tutorial_page, true)
                .build()
            val direction = FragmentLandingDirections.navigateToOperator()
            findNavController().navigate(direction,navOptions)
        }


        editor.putBoolean("is_tutorial_completed?",true)
        editor.apply()

        autoPopulateButton.setOnClickListener {
            if (userNameEditText.text.toString() != "" && passwordEditText.text.toString() != ""){
                jobNamePopulateMethod = "auto"
                autoPopulateJobs(userNameEditText.text.toString(),passwordEditText.text.toString())
            } else{
                Snackbar.make(
                    requireView(),
                    "Invalid1",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }

        manualPopulateButton.setOnClickListener {
            if (!manualEditFlag){
                addWorkplaceButton.visibility = View.INVISIBLE
                addWorkPlaceEditText.visibility= View.INVISIBLE
                manualEditFlag = true
                jobNamePopulateMethod = "manual"
            } else{
                addWorkplaceButton.visibility = View.VISIBLE
                addWorkPlaceEditText.visibility= View.VISIBLE
                manualEditFlag = false
                jobNamePopulateMethod = "auto"
            }
        }

        tutorialButton.setOnClickListener{
            editor.putBoolean("is_tutorial_completed?",false)
            editor.apply()
            ShowTutorialDialog().show(childFragmentManager, "TUTORIAL_DIALOG")

        }

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
                addWorkplaceButton,
                rootView
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

        submitFormButton.setOnClickListener {

            val checkValidation = validateForm(userNameEditText,
                    passwordEditText,
                    addWorkPlaceEditText,
                    rootView,
                    jobNamePopulateMethod)

            if (jobNamePopulateMethod == "manual"){
                if (checkValidation){
                    Snackbar.make(
                        requireView(),
                        "Please input the required fields",
                        Snackbar.LENGTH_LONG
                    ).show()
                }else{
                    var insertObj = OperatorRepo(0,addWorkPlaceEditText.text.toString())
                    editor.putString("username",userNameEditText.text.toString())
                    editor.putString("password", EncryptionHelper.encrypt(passwordEditText.text.toString()))
                    editor.apply()
                    viewModelO.addOperator(insertObj)
                    if (editTextTrack.size>0){
                        for (i in 0..<editTextTrack.size){
                            insertObj = OperatorRepo(0,rootView.findViewById<EditText>(editTextTrack.elementAt(i)).text.toString())
                            viewModelO.addOperator(insertObj)
                        }
                        editor.putBoolean("is_user_logged_in?",true)
                        editor.apply()

                        val directions = FragmentLandingDirections.navigateToOperator()
                        findNavController().navigate(directions)
                    }

                }
            } else {
                if (checkValidation){
                    Snackbar.make(
                        requireView(),
                        "Please input the required fields",
                        Snackbar.LENGTH_LONG
                    ).show()
                }else {
                    if (jobNameList.isNotEmpty()) {
                        var insertObj = OperatorRepo(0, "")
                        for (i in jobNameList) {
                            Log.d("job names:",i)
//                            insertObj = OperatorRepo(0, i)
//                            viewModelO.addOperator(insertObj)
                        }
                        editor.putBoolean("is_user_logged_in?",true)
                        editor.apply()

                        val directions = FragmentLandingDirections.navigateToOperator()
                        findNavController().navigate(directions)
                    }else{
                        Snackbar.make(
                            requireView(),
                            "No Valid jobs found, please populate the jobs, if it is not getting populated, please contact admin!!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }




        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

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
                                   rootView: View
                                   ){

            if (editTextTrack.isNotEmpty() && buttonTrack.isNotEmpty()) {
                addNewWorkplaceEditText.updateLayoutParams<LayoutParams> {
                    marginEnd = paramsAddWorkPlace.marginEnd
                    marginStart = paramsAddWorkPlace.marginStart - 75
                    startToStart = paramsAddWorkPlace.startToStart
                    topToBottom = editTextTrack.last()
                    endToStart = clearNewWorkPlaceEditTextButton.id
                    width = addWorkPlaceEditText.width
                }
                editTextTrack.add(addNewWorkplaceEditText.id)
                clearNewWorkPlaceEditTextButton.updateLayoutParams<LayoutParams> {
                    marginEnd = paramsAddTextFieldButton.marginEnd
                    topMargin = 50
                    startToEnd = addNewWorkplaceEditText.id
                    topToBottom = buttonTrack.last()
                    endToEnd = LayoutParams.PARENT_ID
                }
                buttonTrack.add(clearNewWorkPlaceEditTextButton.id)
            } else {
                addNewWorkplaceEditText.updateLayoutParams<LayoutParams> {
                    marginEnd = paramsAddWorkPlace.marginEnd
                    marginStart = paramsAddWorkPlace.marginStart - 75
                    startToStart = paramsAddWorkPlace.startToStart
                    topToBottom = addWorkPlaceEditText.id
                    endToStart = clearNewWorkPlaceEditTextButton.id
                    width = addWorkPlaceEditText.width
                }
                editTextTrack.add(addNewWorkplaceEditText.id)
                clearNewWorkPlaceEditTextButton.updateLayoutParams<LayoutParams> {
                    marginEnd = paramsAddTextFieldButton.marginEnd
                    topMargin = 50
                    startToEnd = addNewWorkplaceEditText.id
                    topToBottom = addWorkplaceButton.id
                    endToEnd = LayoutParams.PARENT_ID
                }
                buttonTrack.add(clearNewWorkPlaceEditTextButton.id)
            }

    }

    private fun validateForm(userNameEditText:EditText,
                             passwordEditText:EditText,
                             addWorkPlaceEditText:EditText,
                             rootView:View,
                             jobNamePopulateMethod:String):Boolean{

        var validation = false

        if (userNameEditText.text.toString() == ""){
            userNameEditText.hint = "Required!!!"
            validation = true
        }
        if (passwordEditText.text.toString() == ""){
            passwordEditText.hint = "Required!!!"
            validation = true
        }

        if (jobNamePopulateMethod == "manual"){

            if (addWorkPlaceEditText.text.toString() == ""){
                addWorkPlaceEditText.hint = "Required!!!"
                validation = true
            }

            if(editTextTrack.size>0){
                for (i in 0..<editTextTrack.size){
                    if(rootView.findViewById<EditText>(editTextTrack[i]).text.toString()==""){
                        rootView.findViewById<EditText>(editTextTrack[i]).hint = "Enter name or remove field."
                        validation = true
                    }
                }
            }
        }

        return validation


    }

    private fun autoPopulateJobs(checkUsername:String?,checkPassword:String?){
        getClickVisuals(checkUsername,checkPassword)

    }

    private fun getClickVisuals(checkUsername:String?,checkPassword:String?){
        webView.visibility = View.VISIBLE
        webView.setWebViewClient(object : WebViewClient(){
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:(function(){" +
                        "document.getElementsByClassName('btn btn-large btn-primary color-active')[0].click();" +
                        "})()")
                fillFields(checkUsername.toString(),checkPassword.toString())
                pressLogin()
                getDuoCode()
                retrieveJobName()
            }
        })

        webView.loadUrl("https://mytime.oregonstate.edu/")
        webView.settings.javaScriptEnabled = true
    }

    private fun fillFields(username:String,password:String){
        Log.d("fillFields", username+password)
        val javascript = """
                    (function() {
                        document.getElementById('username').value = '${username}';
                        document.getElementById('password').value = '${password}';
                    })();
                """.trimIndent()
        webView.evaluateJavascript(javascript, null)
    }
    //    document.getElementById('password').value = '${EncryptionHelper.decrypt(password)}';
    private fun pressLogin(){

        val javascript = """
                   (function() {
                       var buttons = document.getElementsByName('_eventId_proceed');
                       if (buttons.length > 0) {
                           buttons[0].click();
                       } else {
                           console.error('Button with name "abc" not found.');
                       }    
                       //if (Object.values(document.getElementsByClassName('form-element form-error')).at(0).innerText === "Incorrect username or password.") {
                           //return true;
                       //}
                       
                   })();
               """.trimIndent()
        webView.evaluateJavascript(javascript, null)
    }

    private fun getDuoCode(){

        val javascript = """
                   (function() {
                            return new Promise(function(resolve, reject) {
                                var duoCode = '';
                                setTimeout(function() {
                                    duoCode = Object.values(document.getElementsByClassName('row display-flex align-flex-justify-content-center verification-code')).at(0).innerText;
                                    if (duoCode) {
                                        DuoCodeInterface.receiveValue(duoCode);
                                        resolve(duoCode);
                                    } else {
                                        reject("Error occurred");
                                    }
                                }, 1000);
                            });
                        })();
               """.trimIndent()

        webView.addJavascriptInterface(
            JavaScriptCallbackInterface { result ->Log.d("list",result)
                ShowDuoCodeDialog(result).show(childFragmentManager,"DUOCODE_DIALOG")
            },
            "DuoCodeInterface"
        )

        webView.evaluateJavascript(javascript,null)
    }
    private fun retrieveJobName(){

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()

        val javascript = """
                   (function() {
                            return new Promise(function(resolve, reject) {
                                var jobName = [];
                                var joinJobName = '';
                                setTimeout(function() {
                                    var container = document.querySelector("div#ext-element-45")
                                    var jobs=container.querySelectorAll("div[id^='ext-simplelist']")
                                    for(let i =0;i<jobs.length;i++){
                                        jobName.push(jobs[i].innerText)
                                    }
                                    joinJobName = jobName.join(',')
                                    if (joinJobName) {
                                    JobNameInterface.receiveValue(joinJobName);
                                    resolve(joinJobName);
                                } else {
                                    reject("Error occurred in jobname");
                                }
                                }, 1000);                 
                            });
                        })();
                   """.trimIndent()


                webView.addJavascriptInterface(
                    JavaScriptCallbackInterface { result ->
                        editor.putString("fetched_jobs",result)
                        editor.apply()
                        ShowJobNameDialog(result,jobNameList).show(childFragmentManager, "JOBNAME_DIALOG")
                    },
                    "JobNameInterface"
                )



        webView.evaluateJavascript(javascript,null)
    }


}

class JavaScriptCallbackInterface(private val callback: (String) -> Unit) {
    @JavascriptInterface
    fun receiveValue(value: String) {
        callback(value)
    }
}

class JavaScriptCallbackInterfaceJobName(private val callback: (String) -> Unit) {
    @JavascriptInterface
    fun receiveJobNameValue(value: String) {
        callback(value)
    }
}