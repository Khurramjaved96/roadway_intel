package com.example.noor.scproject.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.noor.scproject.R;

/**
 * Created by noor on 5/30/17.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
