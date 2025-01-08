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

public class MainActivity extends AppCompatActivity {

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


        booksRecyclerView = findViewById(R.id.booksRecView);
        addButton = findViewById(R.id.add_new_book_btn);
        numBooksReadTV = findViewById(R.id.read_books);
        totalNumBooksTV = findViewById(R.id.total_books);

        androidID = DeviceUtilities.getAndroidID(this);

        ArrayList<Book> books = new ArrayList<>();

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
                setRecViews(books);
                setBookCountValues();
                adapter = new ParentBookRecViewAdapter(recViews, getApplicationContext());
                booksRecyclerView.setAdapter(adapter);
                booksRecyclerView.setLayoutManager((new LinearLayoutManager(getApplicationContext())));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Unable to get books: " + error, Toast.LENGTH_SHORT).show();
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
