package com.example.readinglist;

public class Book {
    private String bookName;
    private String author;
    private int pubYear;
    private String ISBN;
    private String genre;
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

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
