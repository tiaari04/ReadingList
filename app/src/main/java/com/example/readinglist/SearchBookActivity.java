package com.example.readinglist;

import static org.json.JSONObject.NULL;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SearchBookActivity extends AppCompatActivity {

    private TextView noResults;
    private EditText searchBar;
    private RecyclerView booksFound;
    private Button backButton;
    private ProgressBar progressBar;
    private ImageButton searchButton;
    private ArrayList<SearchedBookInfo> searchedBooks;
    private RequestQueue requestQueue;

    private Button filterButton;
    private EditText authorFilter;
    private EditText isbnFilter;
    private Button hideBtn;

    private String title, author, isbn;
    private boolean queryTitle, queryAuthor, queryISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_book);

        noResults = findViewById(R.id.search_textview);
        searchBar = findViewById(R.id.search_bar);
        booksFound = findViewById(R.id.searchRecView);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);
        searchButton = findViewById(R.id.search_button);

        filterButton = findViewById(R.id.filter_btn);
        authorFilter = findViewById(R.id.filter_author);
        isbnFilter = findViewById(R.id.filter_isbn);

        hideBtn = findViewById(R.id.hide_btn);

        searchedBooks = new ArrayList<>();

        SearchedBookAdapter adapter = new SearchedBookAdapter(this);
        adapter.setBooks(searchedBooks);

        queryTitle = false;
        queryAuthor = false;
        queryISBN = false;

        booksFound.setLayoutManager((new LinearLayoutManager(this, RecyclerView.VERTICAL, false)));
        booksFound.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAuthor = false;
                queryTitle = false;
                queryISBN = false;
                searchedBooks.clear();


                progressBar.setVisibility(View.VISIBLE);

                collectInput();
                String query = buildQuery();

                // if the search query is not empty then we are
                // calling get book info method to load all
                // the books from the API.
                booksInfo(query);
                adapter.notifyDataSetChanged();

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorFilter.setVisibility(View.VISIBLE);
                isbnFilter.setVisibility(View.VISIBLE);
                hideBtn.setVisibility(View.VISIBLE);
                filterButton.setVisibility(View.GONE);
            }
        });

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorFilter.setVisibility(View.GONE);
                isbnFilter.setVisibility(View.GONE);
                hideBtn.setVisibility(View.GONE);
                filterButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void booksInfo(String url) {
        noResults.setVisibility(View.GONE);
        booksFound.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // Initialize the request queue only once
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(SearchBookActivity.this);
        }

        // Clear cache to avoid old responses
        requestQueue.getCache().clear();

        JsonObjectRequest booksObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            int numResults = response.optInt("totalItems");
                            if (numResults == 0) {booksFound.setVisibility(View.INVISIBLE);
                                noResults.setVisibility(View.VISIBLE);
                            }
                            else {
                                noResults.setVisibility(View.GONE);
                                booksFound.setVisibility(View.VISIBLE);

                                JSONArray itemsArray = response.getJSONArray("items");
                                for (int i = 0; i < itemsArray.length(); i++) {
                                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");

                                    String title;
                                    if (volumeObj.has("title")) {
                                        title = volumeObj.optString("title");
                                    }
                                    else {
                                        title = null;
                                    }

                                    JSONArray authors;
                                    ArrayList<String> authorsArray = new ArrayList<>();
                                    if (volumeObj.has("authors")) {
                                        authors = volumeObj.getJSONArray("authors");
                                        if (authors.length() != 0) {
                                            for (int j = 0; j < authors.length(); j++) {
                                                authorsArray.add(authors.optString(j));
                                            }
                                        }
                                    }

                                    String publishedDate;
                                    String pubYear;
                                    if (volumeObj.has("publishedDate")) {
                                        publishedDate = volumeObj.optString("publishedDate");
                                        String[] parts = publishedDate.split("-");
                                        pubYear = parts[0];
                                    }
                                    else {
                                        pubYear = null;
                                    }

                                    int pageCount;
                                    if (volumeObj.has("pageCount")) {
                                        pageCount = volumeObj.optInt("pageCount");
                                    }
                                    else {
                                        pageCount = Integer.parseInt(null);
                                    }

                                    String description;
                                    if (volumeObj.has("description")) {
                                        description = volumeObj.optString("description");
                                    }
                                    else {
                                        description = null;
                                    }
                                    //JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                                    //String thumbnail;
                                    /*if (imageLinks != NULL) {
                                        thumbnail = imageLinks.optString("smallThumbnail");
                                        // change http to https
                                        thumbnail = thumbnail.substring(0, 4) + 's' + thumbnail.substring(4);
                                    }
                                    else {
                                        thumbnail = "https://recsports.utk.edu/wp-content/uploads/sites/46/2018/05/Image-not-available_1-1-800x800.jpg";
                                    }*/

                                    String thumbnail;
                                    if (volumeObj.has("imageLinks")) {
                                        if (volumeObj.optJSONObject("imageLinks").has("smallThumbnail")) {
                                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                                            thumbnail = imageLinks.optString("smallThumbnail");
                                            // change http to https
                                            thumbnail = thumbnail.substring(0, 4) + 's' + thumbnail.substring(4);
                                        }
                                        else {
                                            thumbnail = null;
                                        }
                                    }
                                    else {
                                        thumbnail = null;
                                    }

                                    JSONArray genres;
                                    ArrayList<String> genreArray = new ArrayList<>();
                                    if (volumeObj.has("categories")) {
                                        genres = volumeObj.getJSONArray("categories");
                                        if (genres.length() != 0) {
                                            for (int j = 0; j < genres.length(); j++) {
                                                genreArray.add(genres.optString(j));
                                            }
                                        }
                                    }

                                /*JSONArray industryIds = volumeObj.getJSONArray("industryIdentifiers");
                                for (JSONObject obj : industryIds) {
                                    if (obj.)
                                }
                                JSONObject industryIdsObj = industryIds.getJSONObject();*/

                                    SearchedBookInfo bookInfo = new SearchedBookInfo(title, authorsArray, pubYear, genreArray, description, pageCount, thumbnail);
                                    searchedBooks.add(bookInfo);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchBookActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(booksObjectRequest);
    }

    public void collectInput() {
        title = searchBar.getText().toString();
        author = authorFilter.getText().toString();
        isbn = isbnFilter.getText().toString();

        // ensure that user input bar is not empty
        if (title.isEmpty() && author.isEmpty() && isbn.isEmpty()) {
            searchBar.setError("Required: one of title, author or ISBN");
            authorFilter.setError("Required: one of title, author or ISBN");
            isbnFilter.setError("Required: one of title, author or ISBN");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            if (!title.isEmpty()) {queryTitle = true;}
            if (!author.isEmpty()) {queryAuthor = true;}
            if (!isbn.isEmpty()) {
                if (isbn.trim().length() != 13) {
                    isbnFilter.setError("The ISBN should be 13 digits");
                }
                else {
                    queryISBN = true;
                }
            }
        }
    }

    public String buildQuery() {
        String query = "https://www.googleapis.com/books/v1/volumes?q=";
        if (queryTitle) {
            query = query.concat("intitle:" + changeQueryString(title));
            //query = query.concat("+intitle:" + title);

        }
        if (queryAuthor) {
            query = query.concat("inauthor:" + changeQueryString(author));
            //query = query.concat("+inauthor:" + author);
        }
        if (queryISBN) {
            query = query.concat("isbn:" + isbn);
        }

        query = query.concat("&key=AIzaSyBsFway_No563HaVDieoaixsaG8ocK6t7w");
        Log.d("query", query);
        return query;
    }

    private String changeQueryString(String str) {
        return '"' + str.replaceAll(" ", "+") + '"';
    }
}

// https://www.geeksforgeeks.org/how-to-build-a-book-library-app-using-google-books-api-in-android/
// https://developers.google.com/books/docs/v1/using