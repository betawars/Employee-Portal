package com.example.employeeportal
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfig: AppBarConfiguration
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val openFirstTime = sharedPrefs.getBoolean("first_time", true)
        if(openFirstTime){
            val editor: SharedPreferences.Editor = sharedPrefs.edit()

            editor.putBoolean("first_time",false)
            editor.putBoolean("is_tutorial_completed?",false)
            editor.putBoolean("is_user_logged_in?",false)
            editor.apply()
        }


    }
}