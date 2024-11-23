package com.librarymanagement;

public class BorrowedBook {
    private String title;
    private String borrowDate;
    private String returnDate;
    private String user;

    public BorrowedBook(String title, String borrowDate, String returnDate, String user) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
