package com.libraryapp.ActivityControllers;

import static com.libraryapp.Utilities.Constants.GET_ALL_BOOKS_URL;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.libraryapp.R;
import com.libraryapp.Utilities.RESTController;
import com.libraryapp.domain.Books;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private String userId;
    ListView booksList;
    ArrayAdapter<Books> arrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userInfo");

        booksList = findViewById(R.id.booksList);
        Button confirm = findViewById(R.id.confirmB);
        confirm.setEnabled(false);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String response = null;
            response = RESTController.sendGet(GET_ALL_BOOKS_URL);
            String finalResponse = response;

            handler.post(() -> {
                if (finalResponse != null && !finalResponse.equals("Error")) {
                    try {
                        JSONArray obj = new JSONArray(finalResponse);

                        List<Books> books = new ArrayList<>();
                        for (int i = 0; i < obj.length(); i++) {
                            int id = obj.getJSONObject(i).getInt("id");
                            String title = obj.getJSONObject(i).getString("title");
                            String authorList = "";
                            JSONArray authors = obj.getJSONObject(i).getJSONArray("authorsList");
                            for (int x = 0; x < authors.length(); x++) {
                                authorList += ",  " + authors.getJSONObject(x).getString("authorName");
                            }
                            if (obj.getJSONObject(i).getString("availability").equals("AVAILABLE")) {
                                books.add(new Books(id, title, authorList));
                            }
                        }
                        books.sort(Comparator.comparing(Books::getId));
                        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, books);
                        booksList.setAdapter(arrayAdapter);

                        booksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                confirm.setEnabled(false);
                                for (int i = 0; i < booksList.getCount(); i++) {
                                    if (booksList.isItemChecked(i)) {
                                        confirm.setEnabled(true);
                                        break;
                                    }
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        });
    }
}