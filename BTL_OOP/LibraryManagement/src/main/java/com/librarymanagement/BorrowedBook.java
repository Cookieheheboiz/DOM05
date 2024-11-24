package com.librarymanagement;

public class BorrowedBook {
    private String title;
    private String borrowDate;
    private String returnDate;
    private int id;
    public BorrowedBook(String title, String borrowDate, String returnDate) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public BorrowedBook(int id, String title, String borrowDate, String returnDate) {
        this.title = title;
        this.id=id;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getId() {
            return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
