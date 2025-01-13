package com.example.readinglist;

public class Book {
    private String bookName;
    private String author;
    private String ISBN;
    private int isRead;
    private String imgURL;

    public Book(String bookName, String author, int isRead, String imgURL, String ISBN) {
        this.bookName = bookName;
        this.author = author;
        this.isRead = isRead;
        this.imgURL = imgURL;
        this.ISBN = ISBN;
    }


    public String getBookName() {
        return bookName;
    }

    public int getIsRead() {
        return isRead;
    }

    public String getAuthor() {
        return author;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getISBN() {
        return ISBN;
    }
}
