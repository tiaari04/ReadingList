package com.example.readinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookDetailsActivity extends AppCompatActivity {

    private String title, authors, publishedDate, description, pageCount,  thumbnail, ISBN, genres;

    private TextView titleTV, authorsTV, genresTV, ISBNTV, descTV, pageTV, publishDateTV;
    private Button backToSearchBtn, addToLibBtn;
    private ImageView bookIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);

        titleTV = findViewById(R.id.bookName_bd);
        descTV = findViewById(R.id.description);
        pageTV = findViewById(R.id.page_count_bd);
        authorsTV = findViewById(R.id.bookAuthor_bd);
        genresTV = findViewById(R.id.genre_bd);
        ISBNTV = findViewById(R.id.isbn_bd);
        publishDateTV = findViewById(R.id.pubYear_bd);
        backToSearchBtn = findViewById(R.id.back_to_search_btn);
        addToLibBtn = findViewById(R.id.add_to_lib_btn);
        bookIV = findViewById(R.id.image_bd);

        title = getIntent().getStringExtra("title");
        authors = getIntent().getStringExtra("authors");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getStringExtra("pageCount");
        thumbnail = getIntent().getStringExtra("thumbnail");
        ISBN = getIntent().getStringExtra("ISBN");
        genres = getIntent().getStringExtra("genres");

        titleTV.setText(title);
        publishDateTV.setText("Published In : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Page Count: " + pageCount);
        authorsTV.setText("By " + authors);
        genresTV.setText("Genre(s): " + genres);
        ISBNTV.setText("ISBN-13: " + ISBN);

        Glide.with(this)
                .asBitmap()
                .load(thumbnail)
                .into(bookIV);

        addToLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backToSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}