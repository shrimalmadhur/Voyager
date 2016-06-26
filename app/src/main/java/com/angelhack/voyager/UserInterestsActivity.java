package com.angelhack.voyager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class UserInterestsActivity extends AppCompatActivity {

    CheckBox chkHiking, chkArt, chkArchitecture, chkPhoto, chkSports;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interests);

        addListenerOnChkIos();
        addListenerOnButton();
    }

    public void addListenerOnChkIos() {

        chkHiking = (CheckBox) findViewById(R.id.checkbox_hiking);

        chkHiking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(UserInterestsActivity.this,
                            "Nice Hobby", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void addListenerOnButton() {

        chkArt = (CheckBox) findViewById(R.id.checkbox_art);
        chkArchitecture = (CheckBox) findViewById(R.id.checkbox_history);
        chkSports = (CheckBox) findViewById(R.id.checkbox_sports);
        chkPhoto = (CheckBox) findViewById(R.id.checkbox_photo);
        chkHiking = (CheckBox) findViewById(R.id.checkbox_hiking);

    }


    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
