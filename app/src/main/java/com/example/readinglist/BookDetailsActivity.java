package com.example.readinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


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

        if (title == null) {
            titleTV.setText("No title found");
        }
        else {
            titleTV.setText(title);
        }

        if (publishedDate == null) {
            publishDateTV.setText("Published In : No year found");
        }
        else {
            publishDateTV.setText("Published In : " + publishedDate);
        }

        if (description == null) {
            descTV.setText("No description found");
        }
        else {
            descTV.setText("Description:\n\t" + description);
        }

        if (pageCount == null || Integer.parseInt(pageCount) == 0) {
            pageTV.setText("No page count found");
        }
        else {
            pageTV.setText("Page Count: " + pageCount);
        }

        if (authors.length() == 0) {
            authorsTV.setText("No authors found");
        }
        else {
            authorsTV.setText("By " + authors);
        }

        if (genres.length() == 0) {
            genresTV.setText("No genre found");
        }
        else {
            genresTV.setText("Genre(s): " + genres);
        }

        if (ISBN == null) {
            ISBNTV.setText("No ISBN found");
        }
        else {
            ISBNTV.setText("ISBN-13: " + ISBN);
        }

        if (thumbnail == null) {
            bookIV.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_image_not_found));
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(thumbnail)
                    .into(bookIV);
        }

        addToLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookToUserLibrary();
            }
        });

        backToSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addBookToUserLibrary() {
        String androidID = DeviceUtilities.getAndroidID(this);

        // check if the book already exists
        HttpHelper.checkBookExists(this, androidID, title, authors, new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean exists) {
                if (!exists) {
                    HttpHelper.addUserBook(BookDetailsActivity.this, androidID, title, authors, thumbnail, ISBN, 0, new Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Book added to library", Toast.LENGTH_SHORT).show();
                                navigateToMainActivity(); // Navigate after successfully adding the book
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(), "Unable to add book: " + error, Toast.LENGTH_SHORT).show());
                                    navigateToMainActivity();
                        }
                    });
                }
                else {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Book already in library", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity(); // Navigate if the book is already in the library
                    });                }
            }
            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Unable to check for book: " + error, Toast.LENGTH_SHORT).show());
                        navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity to prevent going back here
    }
}