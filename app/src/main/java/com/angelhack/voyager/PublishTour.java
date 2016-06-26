package com.angelhack.voyager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.plumillonforge.android.chipview.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PublishTour extends AppCompatActivity implements Chip{

    private String mName;
    private int mType = 0;

    public PublishTour(){

    }

    public PublishTour(String name, int type) {
        this(name);
        mType = type;
    }

    public PublishTour(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_tour);
//        List<Chip> chipList = new ArrayList<>();
//        chipList.add(new PublishTour("Lorem"));
//        chipList.add(new PublishTour("Ipsum dolor"));
//        chipList.add(new PublishTour("Sit amet"));
//        chipList.add(new PublishTour("Consectetur"));
//        chipList.add(new PublishTour("adipiscing elit"));
//        ChipView chipDefault = (ChipView) findViewById(R.id.chipview);
//        chipDefault.setChipList(chipList);
    }

    public void publishTour(View view) {
        Toast.makeText(this, "Tour published", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


}

