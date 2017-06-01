package com.example.noor.scproject.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import com.example.noor.scproject.utilities.ExifOrientationUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.noor.scproject.MainActivity.TAG;

/**
 * Created by noor on 5/31/17.
 */

public class ImageHandler {


    String imagePath = "";  //path of the current image
    ExifOrientationUtil exifOrientationUtil = new ExifOrientationUtil();  //exifOrientationUtil for setting image orientation


    /**
     *
     * @return current photo path
     */
    public String getImagePath() {
        return imagePath;
    }


    /**
     * Takes a path as string and sets it to current photo path
     * @param imagePath path for current photo
     *
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    /**
     * Creates image file in the specified directory
     * @param storageDir  Storage directory where image will be created
     * @return image file as bitmap
     * @throws IOException
     */
    public File createImageFile(File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";   //unique name for file

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        setImagePath("file:" + image.getAbsolutePath());
        return image;

    }


    /**
     * Takes an input image path, rotates it using exifOrientationUtil
     * @param pathname  path of the image
     * @return  reoriented image
     */
    public Bitmap correctOrientationOfBitmap(String pathname){
        File f = new File(pathname);
        Bitmap bmpPic = null;
        try {
            bmpPic = BitmapFactory.decodeStream(new FileInputStream(f), null, null);  //create bitmap of file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return exifOrientationUtil.rotateBitmap(pathname,bmpPic);


    }


    /**
     * Deletes all files in directory
     * @param appDirectory File directory
     */
    public void deleteImages(File appDirectory){
        // Delete all photos taken from camera
        Log.d(TAG, "Photo dir: " + appDirectory.getAbsolutePath());
        File [] allFiles = appDirectory != null ? appDirectory.listFiles() : new File[0];  //get all files in directory

        for (File file: allFiles) {
            if (file.delete()){
                Log.d(TAG, "file Deleted :" + file.getName());
            } else {
                Log.d(TAG, "file not deleted");
            }

        }
        Log.d(TAG, "All images deleted");

    }

    /**
     *
     * @param bitmap bitmap image to be converted to string
     * @return base64 string of image
     */

    public String getStringImage(final Bitmap bitmap){

        ByteArrayOutputStream imageString = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageString); // compress for max quality

        final byte[] imageBytes = imageString.toByteArray();

        Log.d("Roadway Intel", "Image length:" + imageBytes.length);
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
