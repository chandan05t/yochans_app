package com.example.thoughts;

import java.io.IOException;

public interface LoginInBackgroundListener {
    void OnLoginChecked(Boolean Verification);
    void OnFailure(IOException e);
}
