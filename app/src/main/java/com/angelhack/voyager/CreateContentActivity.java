package com.angelhack.voyager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angelhack.voyager.util.MockAction;
import com.angelhack.voyager.util.MockActionCallback;
import com.angelhack.voyager.util.ThreadExecutor;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;


import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateContentActivity extends AppCompatActivity implements FABProgressListener, MockActionCallback {
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    private Toolbar toolbar;
    Uri imageUri = null;
    static TextView imageDetails = null;
    public static ImageView showImg = null;
    CreateContentActivity CameraActivity = null;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    String TAG = "CreateContentActivity";

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private FABProgressCircle fabProgressCircle;
    private boolean taskRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        CameraActivity = this;


        // FAB Listener
        initViews();
        attachListeners();
//            imageDetails = (TextView) findViewById(R.id.imageDetails);


        Button b = (Button) findViewById(R.id.add_card);
        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateContentActivity.this, SecondDestination.class);
                startActivity(intent);
            }
        });

        showImg = (ImageView) findViewById(R.id.showImg);
        showImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateContentActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreateContentActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {

                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(CreateContentActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    /*************************** Camera Intent Start ************************/
                    uploadImage();
                }
            }

        });

//        LinearLayout ll = new LinearLayout(this);
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.contentView);
        mRecordButton = new RecordButton(this);

        ll.setId(View.generateViewId());
        mRecordButton.setId(View.generateViewId());

        RelativeLayout.LayoutParams r1 = new RelativeLayout.LayoutParams(
                120,
                120);

        r1.addRule(RelativeLayout.BELOW, R.id.titleLinearLayout);

        ll.addView(mRecordButton,
                r1);
        mPlayButton = new PlayButton(this);
        RelativeLayout.LayoutParams r2 = new RelativeLayout.LayoutParams(
                120,
                120);

        r2.addRule(RelativeLayout.RIGHT_OF, mRecordButton.getId());
        r2.addRule(RelativeLayout.BELOW, R.id.titleLinearLayout);


        ll.addView(mPlayButton, r2);
        setContentView(ll);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                /*********** Load Captured Image And Data Start ****************/
                String imageId = convertImageUriToFile(imageUri, CameraActivity);


                //  Create and excecute AsyncTask to load capture image
                new LoadImagesFromSDCard().execute("" + imageId);
                /*********** Load Captured Image And Data End ****************/

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /************
     * Convert Image Uri path to physical path
     **************/

    public static String convertImageUriToFile(Uri imageUri, Activity activity) {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String[] proj = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };
            cursor = activity.managedQuery(
                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)
            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


//                imageDetails.setText("No Image");
            } else {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID = cursor.getInt(columnIndex);

                    thumbID = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            + " ImageID :" + imageID + "\n"
                            + " ThumbID :" + thumbID + "\n"
                            + " Path :" + Path + "\n";

                    // Show Captured Image detail on activity
//                    imageDetails.setText(CapturedImageDetails);

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return "" + imageID;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i(TAG, String.valueOf(requestCode));
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    uploadImage();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void uploadImage() {

        /*************************** Camera Intent Start ************************/

        // Define the file-name to save photo taken by Camera activity
        Log.i(TAG, "I am runnning");
        String fileName = "Camera_Example.jpg";

        // Create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

        // imageUri is the current activity attribute, define and save it for later usage
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


        // Standard Intent action that can be sent to have the camera
        // application capture an image and return it.

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void runMockInteractor() {
        ThreadExecutor executor = new ThreadExecutor();
        executor.run(new MockAction(this));
        taskRunning = true;
    }

    @Override
    public void onMockActionComplete() {
        taskRunning = false;
        fabProgressCircle.beginFinalAnimation();
        //fabProgressCircle.hide();
    }



    private void initViews() {
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);

    }

    private void attachListeners() {
        fabProgressCircle.attachListener(this);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!taskRunning) {
                    fabProgressCircle.show();
                    runMockInteractor();
                }
            }
        });
    }

    @Override
    public void onFABProgressAnimationEnd() {
        Log.i(TAG, "clicked");
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                   // mRecordButton.setIm
//                    setText("Stop recording");
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Recording Stoped", Toast.LENGTH_SHORT).show();
                    setBackground(getDrawable(R.drawable.microphone));
//                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
//            setText("Start recording");
            setBackground(getDrawable(R.drawable.microphone));
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setBackground(getDrawable(R.drawable.stop));
                } else {
                    setBackground(getDrawable(R.drawable.play));
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
//            setText("Start playing");
            setBackground(getDrawable(R.drawable.play));
            setOnClickListener(clicker);
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public CreateContentActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    /**
     * Async task for loading the images from the SD card.
     *
     * @author Android Example
     */

    // Class with extends AsyncTask class

    public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(CreateContentActivity.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/

            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {

                /**  Uri.withAppendedPath Method Description
                 * Parameters
                 *    baseUri  Uri to append path segment to
                 *    pathSegment  encoded path segment to append
                 * Returns
                 *    a new Uri based on baseUri with the given segment appended to the path
                 */

                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                    newBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 800, true);
                    bitmap.recycle();
                    if (newBitmap != null) {
                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                // Error fetching image, try to recover
                /********* Cancel execution of this task. **********/
                cancel(true);
            }
            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.
            // Close progress dialog
            Dialog.dismiss();
            if (mBitmap != null) {
                // Set Image to ImageView
                showImg.setImageBitmap(mBitmap);
            }

        }

    }
}
