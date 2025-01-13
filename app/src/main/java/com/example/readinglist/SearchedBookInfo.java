package com.example.readinglist;

import java.util.ArrayList;

public class SearchedBookInfo {
    private String title;
    private ArrayList<String> authors;
    private String publishedDate;
    private ArrayList<String> genres;
    private String ISBN;
    private String description;
    private int pageCount;
    private String thumbnail;

    public SearchedBookInfo(String title, ArrayList<String> authors, String publishedDate, ArrayList<String> genres, String ISBN, String description, int pageCount, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.genres = genres;
        this.ISBN = ISBN;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getNumAuthors() {
        return authors.size();
    }
}
