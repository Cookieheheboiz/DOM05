package com.librarymanagement;

import javafx.scene.image.ImageView;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String category;
    private ImageView coverImage;

    public Book(String title, String author, String publisher, String year, String category, ImageView coverImage) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.category = category;
        this.coverImage = coverImage;
    }

    public Book(String bookTitle, String author, String publisher) {
        this.title = bookTitle;
        this.author = author;
        this.publisher = publisher;
    }


    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getYear() { return year; }
    public String getCategory() { return category; }
    public ImageView getCoverImage() { return coverImage; }
}
