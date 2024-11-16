package com.librarymanagement;

import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String category;
    private ImageView coverImage;
    private String bookID;
    private String bookName;
    private String borrowerName;
    private LocalDate borrowDate;  // Thêm thuộc tính ngày mượn
    private LocalDate returnDate;

    public Book(String title, String author, String publisher, String year, String category, ImageView coverImage) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.category = category;
        this.coverImage = coverImage;
    }

    public Book(String bookID, String bookName, String borrowerName, LocalDate borrowDate, LocalDate returnDate) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;  // Khởi tạo ngày mượn
        this.returnDate = returnDate;  // Khởi tạo ngày trả
    }

    public Book(String bookTitle, String author, String publisher) {
        this.title = bookTitle;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String bookID, String title, String author, String publisher, String category) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getYear() { return year; }
    public String getCategory() { return category; }
    public ImageView getCoverImage() { return coverImage; }
    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;  // Trả về ngày mượn
    }

    public LocalDate getReturnDate() {
        return returnDate;  // Trả về ngày trả
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

    public void setBookID(String bookID) {
        this.bookID = bookID;
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
