package com.angelhack.voyager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    // Create content Activity goes here
    public void createTour(View view) {
        Intent intent = new Intent(this, CreateContentActivity.class);
        startActivity(intent);
    }

    // select a tour Activity goes here
    public void selectTour(View view){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
