package com.example.noor.scproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.noor.scproject.image.ImageHandler;
import com.example.noor.scproject.requests.RequestHandler;
import com.example.noor.scproject.settings.SettingsActivity;
import com.example.noor.scproject.utilities.URIutil;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by noor on 5/30/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Declare constants
     */

    public static String UPLOAD_URL = "http://10.3.16.39:5000/upload";  //server URL
    public static final String IMAGE = "image";  //key in hashmap for image
    public static final String TAG = "ROADWAY INTEL";  //logging tag
    static final int CAMERA = 1;    //if camera is chosen
    private static final int GALLERY = 100;  //if gallery is chosen

    private ImageHandler imageHandler = new ImageHandler();   //handler for image operations
    private static URIutil urIutil = new URIutil();

    private Button recognizeButton;   //recognizer button
    private ImageView imageView;  //image display

    private Bitmap bitmap;   //image bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);  //set layout

        /**
         * get reference to button and image view and set on click listener for recognizer button
         */
        recognizeButton = (Button) findViewById(R.id.recognizeButton);
        recognizeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        recognizeButton.setTextColor(Color.WHITE);
        imageView = (ImageView) findViewById(R.id.imageView);
        recognizeButton.setOnClickListener(this);

        //delete image taken from camera to save memory
        imageHandler.deleteImages(getExternalFilesDir(Environment.DIRECTORY_PICTURES));





    }
    @Override
    public void onClick(View v) {
        //if recognizeButton is clicked, upload image

        if(v == recognizeButton){
            uploadImage();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();  //selected items id


        //open settings activity if settings item selected
        if (id == R.id.action_settings) {
            // Display the fragment as the main content.
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return(true);

        }

        //launch camera activity if camera item selected
        if (id == R.id.action_camera) {
            cameraUpload();
        }

        //launch gallery if gallery item selected

        if (id == R.id.action_gallery) {
            galleryUpload();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA && resultCode == RESULT_OK){
            Log.d(TAG, "Here, image saved at: " + imageHandler.getImagePath());


            Uri photoUri = Uri.parse(imageHandler.getImagePath());
            Bitmap correctOrientation = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                correctOrientation = imageHandler.correctOrientationOfBitmap(photoUri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(correctOrientation);

        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            assert data != null;
            Uri selectedImage = data.getData();
            imageHandler.setImagePath("file:" + urIutil.getPath(this, selectedImage));
            Log.d("Image Detector", "Image selected: " + urIutil.getPath(this, selectedImage));

            Uri photoUri = Uri.parse(imageHandler.getImagePath());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(bitmap);



        }
    }


    /**
     * This function uses getStringImage method of ImageHandler and sets bitmap on UI thread
     * @param bmp bitmap image to be converted to string
     * @return  base64 string of image
     */
    public String getStringImage(final Bitmap bmp){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               imageView.setImageBitmap(bmp);

            }
        });
        return imageHandler.getStringImage(bmp);
    }

    /**
     * This class handles image upload operations
     */
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            private ProgressDialog progressDialog; //for displaying process dialog in ui
            private RequestHandler rh = new RequestHandler();  //for establising connection and sending post request

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //if there is an image to process
                if (imageHandler.getImagePath() != null && imageHandler.getImagePath() != "") {
                    progressDialog = ProgressDialog.show(MainActivity.this, "Processing...","Image is being processed",true,true);

                }

                //get post request parameters from settings
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String serverIP =  sharedPref.getString("ip0","");
                String serverPort = sharedPref.getString("port0","");
                UPLOAD_URL = "http://" + serverIP + ":" + serverPort + "/upload";



            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();  //dismiss process dialog when image is processed
                byte [] decodedImage = Base64.decode(s,Base64.DEFAULT);   //decode post message
                Log.d(TAG, "decodedImage length:" + decodedImage.length);
                imageView.setImageResource(android.R.color.transparent);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);  //get bitmap image from decoded array
                imageView.setImageBitmap(bitmap);   //set bitmap to display
         }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];   //get image
                String uploadImage = getStringImage(bitmap);  //convert bitmap image to string

                HashMap<String,String> data = new HashMap<>();
                data.put(IMAGE, uploadImage);   //put string image in hashmap with key IMAGE

                String result = rh.sendPostRequest(UPLOAD_URL,data);   //send post request on server URL

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        String finalPath = imageHandler.getImagePath().replace("file:", "");  //get path of image
        Bitmap bitmap = BitmapFactory.decodeFile(finalPath);  //get bitmap object from that file path
        ui.execute(bitmap);  //execute async task on bitmap
    }




    /*
*
*  functionality  when the camera is selected as the source of the photo
*
* */
    public void cameraUpload() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //camera intent
        // Check that camera activity is present to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try {
                    //take picture and store it in the file
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", imageHandler.createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }




    /*
*
* functionality  when the gallery is selected as the source of the photo
*
* */
    public void galleryUpload() {

        // get permissions
        //if permission is not granted, request for permission
        //else launch gallery

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //  request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY);

        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY);


        }


    }



    /* Handling permissions and launching gallery activity when permission granted */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Roadway Intel", "EXTERNAL STORAGE permission granted");

            // Permission granted already, so continue work...
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY);

            return;
        }

    }

}
