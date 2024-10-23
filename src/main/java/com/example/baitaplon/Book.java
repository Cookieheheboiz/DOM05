package com.example.baitaplon;

import java.time.LocalDate;

public class Book {
    private String bookID;
    private String bookName;
    private String borrowerName;
    private LocalDate borrowDate;  // Thêm thuộc tính ngày mượn
    private LocalDate returnDate;  // Thêm thuộc tính ngày trả

    // Constructor
    public Book(String bookID, String bookName, String borrowerName, LocalDate borrowDate, LocalDate returnDate) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;  // Khởi tạo ngày mượn
        this.returnDate = returnDate;  // Khởi tạo ngày trả
    }

    // Getter methods
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
}
