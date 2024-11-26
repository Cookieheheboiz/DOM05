package com.librarymanagement;

import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String category;
    private ImageView coverImage;
    private String bookName;
    private String borrowerName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private int quantity;


    public Book(String title, String author, String publisher, String year, String category, ImageView coverImage) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.category = category;
        this.coverImage = coverImage;
    }

    public Book(int id, String bookName, String borrowerName, LocalDate borrowDate, LocalDate returnDate) {
        this.id = id;
        this.bookName = bookName;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Book(String bookTitle, String author, String publisher, int quantity) {
        this.title = bookTitle;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
    }

    public Book(int id, String title, String author, String publisher, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public ImageView getCoverImage() {
        return coverImage;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCoverImage(ImageView coverImage) {
        this.coverImage = coverImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}