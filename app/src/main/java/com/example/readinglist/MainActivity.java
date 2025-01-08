package com.example.readinglist;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private FloatingActionButton addButton;
    private String androidID;
    private BooksRecViewAdapter adapter = new BooksRecViewAdapter(this);


    //private static final String POSTGRES_URL = "http://172.28.5.140:5000/users/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksRecyclerView = findViewById(R.id.booksRecView);
        addButton = findViewById(R.id.fab);

        androidID = DeviceUtilities.getAndroidID(this);

        ArrayList<Book> books = new ArrayList<>();
        //books.add(new Book("It Ends with Us", "Coleen Hoover", 1, "https://images-na.ssl-images-amazon.com/images/I/91CqNElQaKL._AC_UL210_SR210,210_.jpg"));

        adapter.setBooks(books);
        booksRecyclerView.setAdapter(adapter);
        booksRecyclerView.setLayoutManager((new LinearLayoutManager(this)));

        getUserInfo(androidID, books);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create screen that opens to search for a book
                Intent intent = new Intent(getApplicationContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserInfo(String androidID, ArrayList<Book> books) {
        HttpHelper.checkUserExists(this, androidID, new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean exists) {
                if (exists) {
                    getBooks(androidID, books);
                } else if (!exists) {
                    Toast.makeText(getApplicationContext(), "Adding user", Toast.LENGTH_SHORT).show();
                    addNewUser(androidID);
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Unable to check for user: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewUser(String androidID) {
        HttpHelper.addUser(this, androidID);
    }

    private void getBooks(String androidID, ArrayList<Book> books) {
        HttpHelper.getUserBooks(this, androidID, new Callback<ArrayList<Book>>() {
            @Override
            public void onSuccess(ArrayList<Book> result) {
                books.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Unable to get books: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
