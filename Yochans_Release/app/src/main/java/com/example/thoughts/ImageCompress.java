package com.example.thoughts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

/**
 * Created by Chandan T on 08-03-2018.
 */

public class ImageCompress{

    static int height;
    static int width;
    static int Modheight;
    static int ModWidth;

    public static Bitmap getModImage(Bitmap bitmap, int size,File file){

        height = bitmap.getHeight();
        width = bitmap.getWidth();

        if((height > 650 || width > 650) && (size > 250)){
            if(height >= width){
                Modheight = 650;
                ModWidth = 650 * width / height;
            }else{
                ModWidth = 650;
                Modheight = 650 * height / width;
            }

            Bitmap ModBitmap = Bitmap.createBitmap(
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Rect rectangle = new Rect(0,0,ModWidth,Modheight);

            Canvas canvas = new Canvas(ModBitmap);
            canvas.drawBitmap(
                    bitmap,
                    null,
                    rectangle,
                    new Paint(Paint.FILTER_BITMAP_FLAG)
            );

            ModBitmap = Bitmap.createBitmap(ModBitmap,
                    0,
                    0,
                    ModWidth,
                    Modheight
            );

            ExifInterface exif;
            try {
                exif = new ExifInterface(String.valueOf(file));
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                ModBitmap = Bitmap.createBitmap(ModBitmap, 0, 0,ModBitmap.getWidth(),
                        ModBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ModBitmap;
        }else{
            return bitmap;
        }
    }
}
