package com.example.noor.scproject.utilities;

import android.graphics.Bitmap;
import java.io.IOException;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * Created by noor on 5/30/17.
 */

public class ExifOrientationUtil {


    /**
     *
     * @param src  file path
     * @param bitmap  bitmap image
     * @return  bitmap transformed by matrix m
     */

    public static Bitmap rotateBitmap(String src, Bitmap bitmap){
        try{
            int orientation = getExifOrientation(src);
            if(orientation==1)
                return bitmap;

            Matrix matrix = new Matrix();
            /**
             * Determine orientation and set transformation matrix
             */
            switch(orientation){
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;

            }
            try{

                //create new bitmap using transformation matrix
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();  //recycle bitmap to release memory
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }

        }
        catch (IOException e){

        }
        return bitmap;
    }


    /**
     *
     * @param src  file source
     * @return  orientation of file using exif interface
     * @throws IOException
     */
    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {

            ExifInterface exif = new ExifInterface(src);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);  //get orientation of file

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return orientation;
    }


}
