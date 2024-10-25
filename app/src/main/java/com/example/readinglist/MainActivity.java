package com.example.readinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView booksRecyclerView;
    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksRecyclerView = findViewById(R.id.booksRecView);
        addButton = findViewById(R.id.fab);

        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("It Ends with Us", "Coleen Hoover", 2016, "9781501110368", true, "Romance", "https://images-na.ssl-images-amazon.com/images/I/91CqNElQaKL._AC_UL210_SR210,210_.jpg"));

        BooksRecViewAdapter adapter = new BooksRecViewAdapter(this);
        adapter.setBooks(books);

        booksRecyclerView.setAdapter(adapter);
        booksRecyclerView.setLayoutManager((new LinearLayoutManager(this)));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create screen that opens to search for a book
                Intent intent = new Intent(getApplicationContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });
    }
}