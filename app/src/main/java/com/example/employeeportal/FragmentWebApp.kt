package com.example.employeeportal

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager

class FragmentWebApp:Fragment(R.layout.fragment_web_activity) {
    private lateinit var webView: WebView
    private lateinit var username: TextView
    private lateinit var password: TextView
    private lateinit var saveButton: Button
    private lateinit var settingsButton: ImageView
    private var ifError: String = "false"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        webView = view.findViewById(R.id.emp_web_view)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        saveButton = view.findViewById(R.id.save_button)
        settingsButton = view.findViewById(R.id.settings_IV)

        username.hint = getString(R.string.username_hint_text)


        settingsButton.setOnClickListener {
            onSettingsClick()
        }


        val checkUsername = sharedPrefs.getString(getString(R.string.username),"")
        val checkPassword = sharedPrefs.getString(getString(R.string.password),"")

        if (checkPassword == "" && checkUsername == "" && ifError == "false"){
            getCredVisuals()
        }else{
            getClickVisuals(checkUsername,checkPassword)
        }
    }
    private fun onSettingsClick(){
        val directions = FragmentWebAppDirections.navigateToSettings()
        findNavController().navigate(directions)
    }
    @SuppressLint("ResourceType")
    private fun getCredVisuals(){
        ifError = "false"
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        username.visibility = View.VISIBLE
        password.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        webView.visibility = View.INVISIBLE
        username.text = sharedPrefs.getString(getString(R.string.username),"")
        password.text = sharedPrefs.getString(getString(R.string.password),"")
        saveButton.setOnClickListener {
            editor.putString("username",username.text.toString())
            editor.putString("password",EncryptionHelper.encrypt(password.text.toString()))
            editor.apply()
            val checkUsername = sharedPrefs.getString(getString(R.string.username),"")
            val checkPassword = sharedPrefs.getString(getString(R.string.password),"")
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            getClickVisuals(checkUsername,checkPassword)
        }
    }

    private fun getClickVisuals(checkUsername:String?,checkPassword:String?){
        username.visibility = View.INVISIBLE
        password.visibility = View.INVISIBLE
        saveButton.visibility = View.INVISIBLE
        webView.visibility = View.VISIBLE
        webView.setWebViewClient(object : WebViewClient(){
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:(function(){" +
                        "document.getElementsByClassName('btn btn-large btn-primary color-active')[0].click();" +
                        "})()")
                fillFields(checkUsername.toString(),checkPassword.toString())
                pressLogin()
            }
        })

        webView.loadUrl("https://mytime.oregonstate.edu/")
        webView.settings.javaScriptEnabled = true
    }

    private fun fillFields(username:String,password:String){
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
                       if (Object.values(document.getElementsByClassName('form-element form-error')).at(0).innerText === "Incorrect username or password.") {
                           return true;
                       }
                       
                   })();
               """.trimIndent()


        val callback = ValueCallback<String> { value ->
            if (value!="null" || value != null) {
                ifError = value
            } else {
                Log.d("WebView", "Element not found or value is null or blank")
            }
        }

        if (ifError == "false" || ifError == "null" || ifError.isBlank()){
            webView.evaluateJavascript(javascript, callback)

        }else{

            getCredVisuals()
        }
    }
}