package com.example.readinglist;

public class Book {
    private String bookName;
    private String author;
    private int pubYear;
    private String ISBN;
    private String genre;
    private boolean isRead;
    private String imgURL;

    public Book(String bookName, String author, int pubYear, String ISBN, boolean isRead, String genre, String imgURL) {
        this.bookName = bookName;
        this.author = author;
        this.pubYear = pubYear;
        this.ISBN = ISBN;
        this.isRead = isRead;
        this.genre = genre;
        this.imgURL = imgURL;
    }


    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
