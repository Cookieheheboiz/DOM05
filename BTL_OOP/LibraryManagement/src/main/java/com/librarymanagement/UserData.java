package com.librarymanagement;

public class UserData {
    private final Integer id;
    private final String borrowedBook;
    private final String borrowDate;
    private final String returnDate;
    private final String status;
    private final String author;

    public UserData(int id, String author, String borrowedBook, String borrowDate, String returnDate, String status) {
        this.id = id;
        this.author=author;
        this.borrowedBook = borrowedBook;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getAuthor(){
        return author;
    }

    public String getBorrowedBook() {
        return borrowedBook;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
}
