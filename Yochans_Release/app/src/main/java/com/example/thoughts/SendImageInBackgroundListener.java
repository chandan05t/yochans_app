package com.example.thoughts;

import java.io.IOException;

import okhttp3.Response;

public interface SendImageInBackgroundListener {
    void OnImageSaved(Response response);
    void OnFailure(IOException e);
}
