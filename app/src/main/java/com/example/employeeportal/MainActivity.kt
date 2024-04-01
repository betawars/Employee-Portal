package com.example.employeeportal

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var username: TextView
    private lateinit var password: TextView
    private lateinit var saveButton: Button
    private var ifError: String = "false"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.emp_web_view)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        saveButton = findViewById(R.id.save_button)

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val checkUsername = sharedPrefs.getString(getString(R.string.username),"")
        val checkPassword = sharedPrefs.getString(getString(R.string.password),"")

        if (checkPassword == "" && checkUsername == "" && ifError == "false"){
            getCredVisuals()
        }else{
            getClickVisuals(checkUsername,checkPassword)
        }

    }

    private fun getCredVisuals(){
        ifError = "false"
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        username.visibility = View.VISIBLE
        password.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        webView.visibility = View.INVISIBLE
        saveButton.setOnClickListener {
            editor.putString("username",username.text.toString())
            editor.putString("password",EncryptionHelper.encrypt(password.text.toString()))
            editor.apply()
            val checkUsername = sharedPrefs.getString(getString(R.string.username),"")
            val checkPassword = sharedPrefs.getString(getString(R.string.password),"")
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