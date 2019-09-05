package com.example.thoughts;

import org.json.JSONArray;

import java.io.IOException;

public interface FindInBackgroundListener {
    void OnFound(JSONArray DjangoJSONArray);
    void OnFailure(IOException e);
}
