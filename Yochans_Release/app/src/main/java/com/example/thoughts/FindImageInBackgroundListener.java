package com.example.thoughts;

import android.graphics.Bitmap;

import java.io.IOException;

public interface FindImageInBackgroundListener{
    void OnFailure(IOException e);
    void OnFound(Bitmap ImageBitmap, String ImageAddress);
}
