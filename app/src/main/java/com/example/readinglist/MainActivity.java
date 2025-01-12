package com.example.readinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private RecyclerView booksRecyclerView;
    private TextView numBooksReadTV, totalNumBooksTV;
    private Button addButton;
    private String androidID;
    private ParentBookRecViewAdapter adapter;
    private ArrayList<BookRecView> recViews = new ArrayList<>();
    private ArrayList<Book> booksRead = new ArrayList<>();
    private ArrayList<Book> booksUnread = new ArrayList<>();
    private ArrayList<Book> booksReading = new ArrayList<>();
    private int numBooksRead = 0;
    private int totalNumOfBooks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        executorService = Executors.newSingleThreadExecutor();

        booksRecyclerView = findViewById(R.id.booksRecView);
        addButton = findViewById(R.id.add_new_book_btn);
        numBooksReadTV = findViewById(R.id.read_books);
        totalNumBooksTV = findViewById(R.id.total_books);

        androidID = DeviceUtilities.getAndroidID(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create screen that opens to search for a book
                Intent intent = new Intent(getApplicationContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });

        executorService.submit(() -> {
            ArrayList<Book> books = new ArrayList<>();
            getUserInfo(androidID, books);
        });
    }

    private void getUserInfo(String androidID, ArrayList<Book> books) {
        try {
            HttpHelper.checkUserExists(this, androidID, new Callback<Boolean>() {
                @Override
                public void onSuccess(Boolean exists) {
                    if (exists) {
                        executorService.submit(() -> getBooks(androidID, books));
                    } else {
                        executorService.submit(() -> addNewUser(androidID));
                    }
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "Unable to check for user: " + error, Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            runOnUiThread(() ->
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void addNewUser(String androidID) {
        HttpHelper.addUser(this, androidID);
    }

    private void getBooks(String androidID, ArrayList<Book> books) {
        HttpHelper.getUserBooks(this, androidID, new Callback<ArrayList<Book>>() {
            @Override
            public void onSuccess(ArrayList<Book> result) {
                runOnUiThread(() -> {
                    books.addAll(result);
                    setRecViews(books);
                    setBookCountValues();
                    adapter = new ParentBookRecViewAdapter(recViews, MainActivity.this);
                    booksRecyclerView.setAdapter(adapter);
                    booksRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Unable to get books: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void setRecViews(ArrayList<Book> books) {
        categorizeBooks(books);
        recViews.add(new BookRecView("Reading", booksReading));
        recViews.add(new BookRecView("Unread", booksUnread));
        recViews.add(new BookRecView("Read", booksRead));
    }

    private void categorizeBooks(ArrayList<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsRead() == 0) {
                booksUnread.add(books.get(i));
            }
            else if (books.get(i).getIsRead() == 1) {
                booksRead.add(books.get(i));
                numBooksRead++;
            }
            else if (books.get(i).getIsRead() == 2) {
                booksReading.add(books.get(i));
            }
            totalNumOfBooks++;
        }
    }

    private void setBookCountValues() {
        totalNumBooksTV.append(Integer.toString(totalNumOfBooks));
        numBooksReadTV.append(Integer.toString(numBooksRead));
    }
}
