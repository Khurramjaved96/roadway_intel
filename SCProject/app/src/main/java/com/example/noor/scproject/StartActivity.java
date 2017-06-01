package com.example.noor.scproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by noor on 6/1/17.
 */

public class StartActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        final ImageButton im = (ImageButton)findViewById(R.id.start);

        im.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);   //launch level 1
            }

        });




    }
}
