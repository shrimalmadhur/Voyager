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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.plumillonforge.android.chipview.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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

//    https://api.havenondemand.com/1/api/sync/addtotextindex/v1?index=angelhack&apikey=579fb90c-eee0-405b-8fb3-aed94e722343&json={
//            "document" :
//            [
//    {
//        "tags" : "",
//            "tourtitle" : ""
//    }
//    ]}

    public void publishTour(View view) throws Exception {
        Toast.makeText(this, "Tour published", Toast.LENGTH_SHORT).show();

        EditText titleEditText = (EditText) findViewById(R.id.tourtitle);
        String title = titleEditText.getText().toString();

        EditText tagsEditText = (EditText) findViewById(R.id.tags);
        String tags = tagsEditText.getText().toString();

        String BASE_URL = "https://api.havenondemand.com/1/api/sync/addtotextindex/v1?index=angelhack&apikey=579fb90c-eee0-405b-8fb3-aed94e722343&json=";
        JSONObject doc = new JSONObject();

        JSONArray arr = new JSONArray();

        JSONObject data = new JSONObject();

        try {
            data.put("tags", tags);
            data.put("tourtitle", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arr.put(data);
        try {
            doc.put("document", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String full_url = BASE_URL + doc.toString();

        Log.i("PublishTour", full_url);

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL obj = null;
                try {
                    obj = new URL(full_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection con = null;
                try {
                    con = (HttpsURLConnection) obj.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String USER_AGENT = "Mozilla/5.0";
                //add reuqest header
                try {
                    con.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

//                String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = null;
                int responseCode = 0;
                try {
                    wr = new DataOutputStream(con.getOutputStream());
//                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();
                    responseCode = con.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                System.out.println("\nSending 'POST' request to URL : " + full_url);
//                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


//        Log.i("PublishTour", response.toString());
//        //print result
//        System.out.println(response.toString());



        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


}

