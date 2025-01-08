package com.example.readinglist;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(String error);
}

