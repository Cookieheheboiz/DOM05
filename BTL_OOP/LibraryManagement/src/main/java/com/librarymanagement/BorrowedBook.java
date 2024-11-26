package com.librarymanagement;

public class BorrowedBook {
    private String title;
    private String borrowDate;
    private String returnDate;
    private int id;
    private String author;
    private String category;
    private int count;

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    private int tier;

    public String getAuthorr() {
        return author;
    }

    public void setAuthorr(String author) {
        this.author = author;
    }

    public String getCategoryy() {
        return category;
    }

    public void setCategoryy(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BorrowedBook(String title, String borrowDate, String returnDate) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public BorrowedBook(int tier, String title, String author, String category, int count) {
        this.tier = tier;
        this.title = title;
        this.author = author;
        this.category = category;
        this.count = count;
    }

    public BorrowedBook(int id, String title, String borrowDate, String returnDate) {
        this.title = title;
        this.id = id;
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
