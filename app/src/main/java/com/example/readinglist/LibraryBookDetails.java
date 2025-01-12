package com.example.readinglist;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class LibraryBookDetails extends AppCompatActivity {

    private Spinner readStatusSpinner;
    private Button doneBtn, backToLibBtn, deleteFromLibBtn;
    private TextView titleTV, authorTV, readStatusTV, ISBNTV;
    private ImageView thumbnailIV;
    private String title;
    private String authors;
    private String androidID;
    private Book book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library_book_details);

        doneBtn = findViewById(R.id.done_btn);
        readStatusSpinner = findViewById(R.id.read_status_spinner);
        backToLibBtn = findViewById(R.id.back_to_library_btn);
        deleteFromLibBtn = findViewById(R.id.delete_from_lib_btn);
        titleTV = findViewById(R.id.bookName_lbd);
        authorTV = findViewById(R.id.bookAuthor_lbd);
        ISBNTV = findViewById(R.id.ISBN_lbd);
        readStatusTV = findViewById(R.id.read_status_lbd);
        thumbnailIV = findViewById(R.id.image_lbd);

        title = getIntent().getStringExtra("title");
        authors = getIntent().getStringExtra("authors");
        androidID = DeviceUtilities.getAndroidID(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.read_status_list_spinner,
                android.R.layout.simple_spinner_item
        );

        // set the dropdown view resource for the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // assign adapter to spinner
        readStatusSpinner.setAdapter(adapter);

        deleteFromLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });

        backToLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedStatus = readStatusSpinner.getSelectedItem().toString();

                if (selectedStatus.equals("Reading")) {
                    setNewReadStatus(2);
                }
                else if (selectedStatus.equals("Read")) {
                    setNewReadStatus(1);
                }
                else if (selectedStatus.equals("Unread")) {
                    setNewReadStatus(0);
                }
            }
        });

        getBookDetails();
    }

    private void getBookDetails() {
        HttpHelper.retrieveBook(this, androidID, title, authors, new Callback<Book>() {

            @Override
            public void onSuccess(Book result) {
                book = result;
                setBookDetails();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Couldn't generate book details", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private void setBookDetails() {
        if (title == null) {
            titleTV.setText("No title found");
        }
        else {
            titleTV.setText(title);
        }

        int currentReadStatus = book.getIsRead();
        if (currentReadStatus == 0) {
            readStatusTV.append(" Unread");
        }
        else if (currentReadStatus == 1) {
            readStatusTV.append(" Read");
        }
        else if (currentReadStatus == 2) {
            readStatusTV.append(" Reading");
        }

        if (authors.length() == 0) {
            authorTV.setText("No authors found");
        }
        else {
            authorTV.setText("By " + authors);
        }

        if (book.getISBN() == null) {
            ISBNTV.setText("No ISBN found");
        }
        else {
            ISBNTV.setText("ISBN-13: " + book.getISBN());
        }

        if (book.getImgURL() == null) {
            thumbnailIV.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_image_not_found));
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(book.getImgURL())
                    .into(thumbnailIV);
        }
    }

    private void setNewReadStatus(int readStatus) {
        HttpHelper.changeReadStatus(this, androidID, title, authors, readStatus, new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
        });
    }

    private void deleteBook() {
        HttpHelper.deleteUserBook(this, androidID, title, authors, new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LibraryBookDetails.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity to prevent going back here
    }
}