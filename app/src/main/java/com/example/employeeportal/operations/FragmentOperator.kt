package com.example.employeeportal.operations

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeeportal.R
import com.example.employeeportal.encrypter.EncryptionHelper
import com.example.employeeportal.landingPage.FragmentLandingDirections
import com.example.employeeportal.operations.data.OperatorRepo
import com.example.employeeportal.operations.data.OperatorViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ShowInOutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Select an action to perform")
                .setPositiveButton("IN") { dialog, id ->
                    Snackbar.make(
                        requireView(),
                        "Invalid1",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("OUT") { dialog, id ->
                    Snackbar.make(
                        requireView(),
                        "Invalid2",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                .setNeutralButton("Cancel"){dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class ShowDuoCodeDialog(var duoCode:String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Your duo code: $duoCode")
                .setPositiveButton("OK") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class FragmentOperator: Fragment(R.layout.fragment_operator) {

    private lateinit var operatorRecyclerView:RecyclerView
    private val adapter = OperatorAdapter(::onOperatorClick)
    private val viewModelO: OperatorViewModel by viewModels()
    private lateinit var webView:WebView


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.hidden_web_view)
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val checkUsername = sharedPrefs.getString(getString(R.string.username),"")
        val checkPassword = sharedPrefs.getString(getString(R.string.password),"")


        autoPopulateJobs(checkUsername,checkPassword)

//            getClickVisuals(checkUsername,checkPassword)

//        viewModelO.getOperatorList
//        operatorRecyclerView = view.findViewById(R.id.operator_list)
//        operatorRecyclerView.adapter = adapter
//        operatorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        operatorRecyclerView.setHasFixedSize(true)
//        viewModelO.getOperatorList.observe(viewLifecycleOwner) { searchHistory ->
//            adapter.updateList(searchHistory)
//
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                requireActivity().finish()
//            }
//        })


//        val jsScript = JavaScriptBackgroundService()
//        val serviceIntent = Intent(requireContext(),JavaScriptBackgroundService::class.java)
//        jsScript.onStartCommand()


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
                retrieveJobName_nothere()
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
                        document.getElementById('password').value = '${EncryptionHelper.decrypt(password)}';
                    })();
                """.trimIndent()
        webView.evaluateJavascript(javascript, null)
    }
    //    document.getElementById('password').value = '${EncryptionHelper.decrypt(password)}';
    private fun pressLogin(){
        var javascript = ""
        javascript = """
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
        var duoCode:String

        var javascript = """
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
//                ShowDuoCodeDialog(result).show(childFragmentManager,"DUOCODE_DIALOG")
            },
            "DuoCodeInterface"
        )

        webView.evaluateJavascript(javascript,null)
    }

    private fun onOperatorClick(repo: OperatorRepo){
        ShowInOutDialog().show(childFragmentManager, "INOUT_DIALOG")
    }

    private fun retrieveJobName_nothere(){
        var javascript = """
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
            JavaScriptCallbackInterfaceJobName { result -> Log.d("list",result)

//                ShowDuoCodeDialog(result).show(childFragmentManager,"DUOCODE_DIALOG")

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
    fun receiveValue(value: String) {
        callback(value)
    }
}