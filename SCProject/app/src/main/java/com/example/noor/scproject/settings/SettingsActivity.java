package com.example.noor.scproject.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.noor.scproject.R;

/**
 * Created by noor on 5/30/17.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
