package com.example.thoughts;

import java.io.IOException;

import okhttp3.Response;

public interface SaveInBackgroundListener {
    void onSaved(Response response);
    void OnFailure(IOException e);
}
