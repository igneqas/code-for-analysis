package com.libraryapp.ActivityControllers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.libraryapp.R;
import com.libraryapp.Utilities.RESTController;
import com.libraryapp.domain.Books;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.libraryapp.Utilities.Constants.CREATE_ORDER_URL;
import static com.libraryapp.Utilities.Constants.GET_ALL_BOOKS_URL;

public class ConfirmationActivity extends AppCompatActivity {

    private String userId;
    private String []books;
    ListView booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userInfo");
        books = intent.getStringExtra("books").split(";");
        for(String x : books) {
            x = x.trim();
        }

        booksList = findViewById(R.id.confirmedBooksList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ConfirmationActivity.this, android.R.layout.simple_list_item_multiple_choice, books);
        booksList.setAdapter(arrayAdapter);

        for(int i=0; i <booksList.getCount();i++) {
            booksList.setItemChecked(i, true);
        }
    }

    public void createOrder(View view) {
        String data = "?userId=" + userId + "&books=";
        for(int i=0; i <booksList.getCount();i++) {
            if (booksList.isItemChecked(i)) {
                data += booksList.getItemAtPosition(i).toString().split(":")[0].trim() + ",";
            }
        }
        System.out.println(data);
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        String finalData = data;
        executor.execute(() -> {
            String response;
            try {
                response = RESTController.sendPost(CREATE_ORDER_URL, finalData);
            } catch (IOException e) {
                e.printStackTrace();
                response = null;
            }
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            handler.post(() -> {
                toMain();
            });
        });
    }

    public void returnToMain(View view) {
        toMain();
    }

    private void toMain(){
        Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
        intent.putExtra("userInfo", userId);
        startActivity(intent);
    }
}