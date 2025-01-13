package com.example.readinglist;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHelper {
    private static final String BASE_URL = "http://172.23.224.1:5000";

    public static void checkUserExists(Context context, String androidID, Callback<Boolean> callback) {
        String url = BASE_URL + "/user/exists/" + androidID;

        // Create a RequestQueue instance (use a singleton pattern in a real app)
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create the JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the "exists" boolean value from the JSON response
                            boolean userExists = response.getBoolean("exists");
                            if (userExists) {
                            }
                            else {
                            }
                            callback.onSuccess(userExists);
                        } catch (Exception e) {
                            callback.onFailure("Failed to parse response in check user");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
                        Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                        callback.onFailure("Request failed: " + error.getMessage());
                    }
                }
        );

        // Add the request to the request queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

    public static void addUser(Context context, String androidID) {
        // Build the URL for the POST request
        String url = BASE_URL + "/addUser";

        // Create the request body in JSON format
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("android_id", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create the JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Handle the successful response (new user added)
                            Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error adding user", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    public static void addUserBook(Context context, String androidID, String title,
                                   String author, String thumbnail, String ISBN, int readStatus, Callback<Void> callback) {
        // Build the URL for the POST request
        String url = BASE_URL + "/books";

        // Create the request body in JSON format
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("android_id", androidID);
            requestBody.put("title", title);
            requestBody.put("author", author);
            requestBody.put("thumbnail", thumbnail);
            requestBody.put("isbn", ISBN);
            requestBody.put("read_status", Integer.toString(readStatus));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create the JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Handle the successful response (new book added)
                            callback.onSuccess(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure("Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        callback.onFailure("Request failed: " + error.getMessage());
                    }
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    public static void getUserBooks(Context context, String androidID, Callback<ArrayList<Book>> callback) {
        // Create the URL for the GET request
        String url = BASE_URL + "/users/" + androidID + "/books";

        // arraylist of books from database
        ArrayList<Book> books = new ArrayList<>();

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON Array Request to retrieve books
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the successful response (list of books)
                        try {
                            if (response.length() > 0) {
                                // Loop through the array and extract book data
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject book = response.getJSONObject(i);
                                    String title = book.getString("title");
                                    String author = book.getString("author");
                                    String thumbnail = book.getString("thumbnail");
                                    int readStatus = book.getInt("read_status");
                                    String isbn = book.getString("isbn");

                                    // Do something with the book (e.g., display in a list)
                                    // For this example, we'll show a Toast
                                    Book userBook = new Book(title, author, readStatus, thumbnail, isbn);
                                    books.add(userBook);
                                }
                                callback.onSuccess(books);
                            } else {
                                callback.onFailure("Your book list is empty.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing books in getUserBooks", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        callback.onFailure("Request failed" + error.getMessage());
                    }
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonArrayRequest);
    }

    public static void checkBookExists(Context context, String androidID, String title, String author, Callback<Boolean> callback) {
        // Create the URL for the GET request with query parameters
        String url = BASE_URL + "/book/exists/" + androidID + "/" + title + "/" + author;

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON Object Request to check if the book exists
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the response indicates the book exists
                            boolean exists = response.getBoolean("exists");
                            callback.onSuccess(exists);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

    public static void retrieveBook(Context context, String androidID, String title, String author, Callback<Book> callback) {
        // Create the URL for the GET request with query parameters
        String url = BASE_URL + "/book/retrieve/" + androidID + "/" + title + "/" + author;

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON Object Request to check if the book exists
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray retrievedBookArray = response.getJSONArray("book");
                            for (int i = 0; i < retrievedBookArray.length(); i++) {
                                JSONObject retrievedBook = retrievedBookArray.getJSONObject(i);

                                String thumbnail = retrievedBook.getString("thumbnail");
                                String isbn = retrievedBook.getString("isbn");
                                int readStatus = retrievedBook.getInt("read_status");
                                Book book = new Book(title, author, readStatus, thumbnail, isbn);
                                callback.onSuccess(book);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure("Unable to retireve book:" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        Toast.makeText(context, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

    public static void changeReadStatus(Context context, String androidID, String title, String author, int readStatus, Callback<String> callback) {
        // Create the URL for the GET request with query parameters
        String url = BASE_URL + "/book/changeStatus/" + androidID + "/" + title + "/" + author + "/" + readStatus;

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON Object Request to check if the book exists
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseMessage = response.getString("message");
                            callback.onSuccess(responseMessage);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure("Unable to change read status:" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        callback.onFailure("Request failed: " + error.getMessage());
                    }
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

    public static void deleteUserBook(Context context, String androidID, String title, String author, Callback<String> callback) {
        // Create the URL for the GET request with query parameters
        String url = BASE_URL + "/book/delete/" + androidID + "/" + title + "/" + author;

        // Create a RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a JSON Object Request to check if the book exists
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseMessage = response.getString("message");
                            callback.onSuccess(responseMessage);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure("Unable to delete book:" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors (e.g., network error)
                        error.printStackTrace();
                        Toast.makeText(context, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

}
