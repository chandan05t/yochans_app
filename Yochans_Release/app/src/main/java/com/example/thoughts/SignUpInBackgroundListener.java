package com.example.thoughts;

import java.io.IOException;

public interface SignUpInBackgroundListener {
    void OnSignUpChecked(Boolean Verification);
    void OnFailure(IOException e);
}
