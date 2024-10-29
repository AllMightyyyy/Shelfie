package org.example.librarysimulation.model;

public class Note {
    private int id;
    private String bookId;
    private int pageNumber;
    private String note;

    public Note(String bookId, int pageNumber, String note) {
        this.bookId = bookId;
        this.pageNumber = pageNumber;
        this.note = note;
    }

    public Note(int id, String bookId, int pageNumber, String note) {
        this.id = id;
        this.bookId = bookId;
        this.pageNumber = pageNumber;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}