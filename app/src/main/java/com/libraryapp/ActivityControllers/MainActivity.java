package com.libraryapp.ActivityControllers;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.libraryapp.R;
import com.libraryapp.Utilities.RESTController;
import com.libraryapp.domain.Authors;
import com.libraryapp.domain.Books;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.libraryapp.Utilities.Constants.GET_ALL_BOOKS_URL;

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
            String response;
            try {
                response = RESTController.sendGet(GET_ALL_BOOKS_URL);
            } catch (IOException e) {
                e.printStackTrace();
                response = null;
            }
            String finalResponse = response;
            //System.out.println(response);
            handler.post(() -> {
                if (finalResponse != null && !finalResponse.equals("Error")) {
                    try {
                        JSONArray obj = new JSONArray(finalResponse);

                        List<Books> books = new ArrayList<>();
                        for(int i = 0; i < obj.length(); i++) {
                            int id = obj.getJSONObject(i).getInt("id");
                            String title = obj.getJSONObject(i).getString("title");
                            String authorList = "";
                            JSONArray authors = obj.getJSONObject(i).getJSONArray("authorsList");
                            for(int x=0; x<authors.length();x++) {
                                authorList += ",  " + authors.getJSONObject(x).getString("authorName");
                            }
                            if(obj.getJSONObject(i).getString("availability").equals("AVAILABLE")) {
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
                                for(int i=0; i <booksList.getCount();i++) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search for a book");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        booksList = findViewById(R.id.booksList);
        int id = item.getItemId();
        if(id == R.id.account){
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            intent.putExtra("userInfo", userId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openConfirmation(View view) {
        String itemSelected = "";
        for(int i=0; i <booksList.getCount();i++) {
            if (booksList.isItemChecked(i)) {
                itemSelected += booksList.getItemAtPosition(i) + ";";
            }
        }
        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
        intent.putExtra("userInfo", userId);
        intent.putExtra("books", itemSelected);
        startActivity(intent);
    }
}