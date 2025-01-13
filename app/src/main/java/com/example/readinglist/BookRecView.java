package com.example.readinglist;

import java.util.ArrayList;

public class BookRecView {
    private String title;
    private ArrayList<Book> books;

    public BookRecView(String title, ArrayList<Book> books) {
        this.title = title;
        this.books = books;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
}
