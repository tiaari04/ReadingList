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
    private int numAuthors;

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

    public SearchedBookInfo(String title, ArrayList<String> authors, String publishedDate, ArrayList<String> genres, String description, int pageCount, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.genres = genres;
        this.ISBN = "0";
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

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getNumAuthors() {
        return authors.size();
    }

    public int getNumGenres() {
        return genres.size();
    }

    public void setNumAuthors(int numAuthors) {
        this.numAuthors = numAuthors;
    }
}
