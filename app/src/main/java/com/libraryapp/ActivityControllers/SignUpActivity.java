package com.libraryapp.ActivityControllers;

import static com.libraryapp.Utilities.Constants.SIGNUP_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.libraryapp.R;
import com.libraryapp.Utilities.InputFieldValidator;
import com.libraryapp.Utilities.RESTController;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void returnToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void validateSignup(View view) {
        EditText userName = findViewById(R.id.signupUsernameField);
        EditText password = findViewById(R.id.signupPasswordField);
        EditText fullName = findViewById(R.id.signupFullNameField);
        EditText phone = findViewById(R.id.signupPhoneField);
        EditText email = findViewById(R.id.signupEmailField);
        if(!InputFieldValidator.signupFieldsAreEmpty(userName.getText().toString(), password.getText().toString(),
                fullName.getText().toString(), phone.getText().toString(), email.getText().toString())) {
            String data = "?userName=" + userName.getText() + "&password=" + password.getText() + "&fullName=" + fullName.getText()
                    + "&phone=" + phone.getText() + "&email=" + email.getText();

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                String response;
                try {
                    response = RESTController.sendPost(SIGNUP_URL, data);
                } catch (IOException e) {
                    e.printStackTrace();
                    response = null;
                }
                String finalResponse = response;
                handler.post(() -> {
                    if (finalResponse != null && finalResponse.equals("Successful signup")) {
                        Toast myToast = Toast.makeText(this, "Successful signup", Toast.LENGTH_SHORT);
                        myToast.show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("userInfo", finalResponse);
                        startActivity(intent);
                    } else {
                        Toast myToast = Toast.makeText(this, finalResponse, Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "Login fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}