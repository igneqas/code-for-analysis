package com.libraryapp.ActivityControllers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.libraryapp.R;
import com.libraryapp.Utilities.InputFieldValidator;
import com.libraryapp.Utilities.RESTController;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.libraryapp.Utilities.Constants.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void validate(View view) {
        EditText userName = findViewById(R.id.usernameField);
        EditText password = findViewById(R.id.passwordField);
        if(!InputFieldValidator.loginFieldsAreEmpty(userName.getText().toString(), password.getText().toString())) {
            String data = "?userName=" + userName.getText() + "&password=" + password.getText();

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                String response;
                try {
                    response = RESTController.sendPost(LOGIN_URL, data);
                } catch (IOException e) {
                    e.printStackTrace();
                    response = null;
                }
                String finalResponse = response;
                handler.post(() -> {
                    if (finalResponse != null && !finalResponse.equals("Wrong login") && !finalResponse.equals("Error")) {
                        System.out.println("BRUH2");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userInfo", finalResponse);
                        startActivity(intent);
                    } else {
                        System.out.println("BRUH");
                        Toast myToast = Toast.makeText(this, "Wrong login", Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "Login fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}