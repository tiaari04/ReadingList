package com.example.readinglist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        noResults = findViewById(R.id.search_textview);
        searchBar = findViewById(R.id.search_bar);
        booksFound = findViewById(R.id.searchRecView);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);
        searchButton = findViewById(R.id.search_button);

        searchedBooks = new ArrayList<>();

        SearchedBookAdapter adapter = new SearchedBookAdapter(this);
        adapter.setBooks(searchedBooks);

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
                progressBar.setVisibility(View.VISIBLE);

                // checking if our edittext field is empty or not.
                if (searchBar.getText().toString().isEmpty()) {
                    searchBar.setError("Please enter search query");
                    return;
                }
                // if the search query is not empty then we are
                // calling get book info method to load all
                // the books from the API.
                booksInfo(searchBar.getText().toString());
            }
        });
    }

    public void booksInfo(String query) {

        requestQueue = Volley.newRequestQueue(SearchBookActivity.this);

        requestQueue.getCache().clear();

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyBsFway_No563HaVDieoaixsaG8ocK6t7w";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest booksObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            int numResults = response.optInt("totalItems");
                            if (numResults == 0) {
                                progressBar.setVisibility(View.GONE);
                                noResults.setVisibility(View.VISIBLE);
                            }
                            else {
                                noResults.setVisibility(View.GONE);

                                JSONArray itemsArray = response.getJSONArray("items");
                                for (int i = 0; i < itemsArray.length(); i++) {
                                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                                    String title = volumeObj.optString("title");
                                    JSONArray authors = volumeObj.getJSONArray("authors");
                                    ArrayList<String> authorsArray = new ArrayList<>();
                                    if (authors.length() != 0) {
                                        for (int j = 0; j < authors.length(); j++) {
                                            authorsArray.add(authors.optString(j));
                                        }
                                    }
                                    String publishedDate = volumeObj.optString("publishedDate");
                                    String[] parts = publishedDate.split("-");
                                    String pubYear = parts[0];
                                    int pageCount = volumeObj.optInt("pageCount");
                                    String description = volumeObj.optString("description");
                                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                                    String thumbnail = imageLinks.optString("smallThumbnail");

                                    // change http to https
                                    thumbnail = thumbnail.substring(0, 4) + 's' + thumbnail.substring(4);

                                    JSONArray genres = volumeObj.getJSONArray("categories");
                                    ArrayList<String> genreArray = new ArrayList<>();
                                    if (genres.length() != 0) {
                                        for (int j = 0; j < genres.length(); j++) {
                                            genreArray.add(genres.optString(j));
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
        {
            /*@Override
                public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-Android-Package", "com.example.readinglist");
                headers.put("X-Android-Cert", "353561572223829763ADADEC8358F69BEA51A81C");
                return headers;
            }*/
        };
    }
}

// https://www.geeksforgeeks.org/how-to-build-a-book-library-app-using-google-books-api-in-android/
// https://developers.google.com/books/docs/v1/using