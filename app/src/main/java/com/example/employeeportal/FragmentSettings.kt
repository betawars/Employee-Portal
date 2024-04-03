package com.example.employeeportal

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class ShowConfirmationDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you want to clear the credentials?")
                .setPositiveButton("Yes") { dialog, id ->
                    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val editor: SharedPreferences.Editor = sharedPrefs.edit()
                    editor.putString("username","")
                    editor.putString("password","")
                    editor.apply()
                    dismiss()
                }
                .setNegativeButton("No") { dialog, id ->
                    dismiss()
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
class FragmentSettings: PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.settings, rootKey)


        val clearPreferences = findPreference<Preference>("clear_button")
        clearPreferences?.setOnPreferenceClickListener {
            ShowConfirmationDialog().show(childFragmentManager, "CONFIRMATION_DIALOG")

            true
        }

    }

}