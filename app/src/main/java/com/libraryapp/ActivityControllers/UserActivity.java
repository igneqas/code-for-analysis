package com.libraryapp.ActivityControllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.libraryapp.R;
import com.libraryapp.Utilities.InputFieldValidator;
import com.libraryapp.Utilities.MainAdapter;
import com.libraryapp.Utilities.RESTController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.libraryapp.Utilities.Constants.*;

public class UserActivity extends AppCompatActivity {

    String userId;
    ExpandableListView orderList;
    ArrayList<String> listGroup = new ArrayList<>();
    HashMap<String, ArrayList<String>> listChild = new HashMap<>();
    MainAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userInfo");

        EditText password = findViewById(R.id.passwordF);
        EditText name = findViewById(R.id.nameF);
        EditText email = findViewById(R.id.emailF);
        EditText phoneNumber = findViewById(R.id.phoneNumberF);
        EditText username = findViewById(R.id.usernameF);
        orderList = findViewById(R.id.order_list);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String response, response1;
            try {
                response = RESTController.sendGet(GET_USER_URL, userId);
                response1 = RESTController.sendGet(GET_USER_ORDERS_URL, userId);
            } catch (IOException e) {
                e.printStackTrace();
                response = null;
                response1 = null;
            }
            String finalResponse = response;
            String finalResponse1 = response1;
            handler.post(() -> {
                try {
                    JSONObject obj = new JSONObject(finalResponse);
                    password.setText(obj.getString("password"));
                    name.setText(obj.getString("userFullName"));
                    email.setText(obj.getString("email"));
                    phoneNumber.setText(obj.getString("phone"));
                    username.setText(obj.getString("userName"));

                    JSONArray orders = new JSONArray(finalResponse1);
                    for(int i=0;i<orders.length();i++){
                        JSONObject order = orders.getJSONObject(i);
                        System.out.println(order);
                        listGroup.add(order.getString("orderID") + " : " + order.getString("issueDate").substring(0,10));
                        System.out.println(order.getString("orderID") + " : " + order.getString("issueDate").substring(0,10));
                        ArrayList<String> tempBooks = new ArrayList<>();
                        JSONArray books = order.getJSONArray("booksList");
                        for(int j=0;j<books.length();j++) {
                            JSONObject book = books.getJSONObject(j);
                            String authorList = "";
                            JSONArray authors = book.getJSONArray("authorsList");
                            for(int x=0; x<authors.length();x++) {
                                authorList += ", " + authors.getJSONObject(x).getString("authorName");
                            }
                            tempBooks.add(book.getString("title") +  authorList);
                            System.out.println(book.getString("title") + authorList);
                        }
                        listChild.put(listGroup.get(i), tempBooks);
                    }

                    adapter = new MainAdapter(listGroup, listChild);
                    orderList.setAdapter(adapter);
                    orderList.setMinimumHeight(listGroup.size()*50);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void updateUser(View view) {
        EditText password = findViewById(R.id.passwordF);
        EditText name = findViewById(R.id.nameF);
        EditText email = findViewById(R.id.emailF);
        EditText phoneNumber = findViewById(R.id.phoneNumberF);
        EditText username = findViewById(R.id.usernameF);
        if(!InputFieldValidator.fieldIsEmpty(name.getText().toString()) && !InputFieldValidator.fieldIsEmpty(password.getText().toString()) && !InputFieldValidator.fieldIsEmpty(email.getText().toString()) && !InputFieldValidator.fieldIsEmpty(phoneNumber.getText().toString()) && !InputFieldValidator.fieldIsEmpty(username.getText().toString())) {
            String data = null;
            data = userId + "?password=" + password.getText() + "&email=" + email.getText() + "&userFullName=" + name.getText() + "&phone=" + phoneNumber.getText() + "&userName=" + username.getText();

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            String finalData = data;
            executor.execute(() -> {
                String response;
                try {
                    response = RESTController.sendPut(UPDATE_USER_URL, finalData);
                } catch (IOException e) {
                    e.printStackTrace();
                    response = null;
                }
                String finalResponse = response;
                System.out.println(response);
                handler.post(() -> {
                    if (finalResponse.equals("User updated")) {
                        changeToMainActivity();
                    }

                });
            });
        }
    }

    public void signOut(View view) {
        changeToLoginActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete account");
            builder.setMessage("Are you sure you want to delete the account?");
            builder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Executor executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());
                            executor.execute(() -> {
                                String response;
                                try {
                                    response = RESTController.sendDelete(DELETE_USER_URL, userId);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    response = null;
                                }
                                String finalResponse = response;
                                System.out.println(response);
                                handler.post(() -> {
                                    if (finalResponse.equals("User deleted")) {
                                        changeToLoginActivity();
                                    } else System.out.println("Error");
                                });
                            });
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToMain(View view) {
        changeToMainActivity();
    }

    private void changeToLoginActivity() {
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void changeToMainActivity() {
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        intent.putExtra("userInfo", userId);
        startActivity(intent);
    }
}