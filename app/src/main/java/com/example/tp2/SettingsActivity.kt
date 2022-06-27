package com.example.tp2

import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.EditTextPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceActivity
import android.widget.Toast
import com.example.tp2.data.source.remote.api.Provider

class SettingsActivity : PreferenceActivity(), OnPreferenceChangeListener {
    var checkPrefs: CheckBoxPreference? = null
    var modifPrefs: EditTextPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        checkPrefs = findPreference("remember") as CheckBoxPreference
        modifPrefs = findPreference("login") as EditTextPreference
        checkPrefs!!.setOnPreferenceChangeListener(this)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val t = Toast.makeText(this,
            "click check box :" + newValue.toString()
                    + " pref manipulée : " + preference!!.getKey(), Toast.LENGTH_SHORT)
        t.show()
        if (newValue == false) {
            modifPrefs?.setText("")
        }

        if(preference!!.getKey() == "url"){
            Provider.changeUrl(newValue.toString())
        }
        return true
    }
}