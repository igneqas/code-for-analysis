package com.libraryapp.ActivityControllers;

import static com.libraryapp.Utilities.Constants.GET_ALL_BOOKS_URL;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.libraryapp.R;
import com.libraryapp.Utilities.RESTController;
import com.libraryapp.domain.Books;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ListView booksList;
    ArrayAdapter<Books> arrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksList = findViewById(R.id.booksList);
        Button confirmReservationButton = findViewById(R.id.confirmReservationButton);
        confirmReservationButton.setEnabled(false);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String response = RESTController.sendGet(GET_ALL_BOOKS_URL);

            handler.post(() -> {
                if (!response.equals("Error")) {
                    try {
                        JSONArray booksArrayJson = new JSONArray(response);
                        List<Books> availableBooks = new ArrayList<>();

                        parseJsonBookData(booksArrayJson, availableBooks);
                        availableBooks.sort(Comparator.comparing(Books::getId));
                        populateBooksList(confirmReservationButton, availableBooks);
                    } catch (JSONException e) {
                        Log.e("JSONException", e.getMessage());
                    }

                }
            });
        });
    }

    private void populateBooksList(Button confirmReservationButton, List<Books> availableBooks) {
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, availableBooks);
        booksList.setAdapter(arrayAdapter);

        booksList.setOnItemClickListener((parent, view, position, id) -> {
            confirmReservationButton.setEnabled(false);
            for (int i = 0; i < booksList.getCount(); i++) {
                if (booksList.isItemChecked(i)) {
                    confirmReservationButton.setEnabled(true);
                    break;
                }
            }
        });
    }

    private void parseJsonBookData(JSONArray booksArrayJson, List<Books> availableBooks) throws JSONException {
        for (int i = 0; i < booksArrayJson.length(); i++) {
            int bookId = booksArrayJson.getJSONObject(i).getInt("id");
            String bookTitle = booksArrayJson.getJSONObject(i).getString("title");
            String authorList = "";
            JSONArray authors = booksArrayJson.getJSONObject(i).getJSONArray("authorsList");
            for (int x = 0; x < authors.length(); x++) {
                authorList += ",  " + authors.getJSONObject(x).getString("authorName");
            }
            if (booksArrayJson.getJSONObject(i).getString("availability").equals("AVAILABLE")) {
                availableBooks.add(new Books(bookId, bookTitle, authorList));
            }
        }
    }
}