package org.example.librarysimulation.model;

import java.util.Arrays;

public class Book {
    private String id;
    private String title;
    private String authors;
    private byte[] coverImage;
    private String description;
    private String publishedDate;
    private String publisher;
    private boolean isFavorite;
    private boolean intendingToRead;
    private int currentPage;

    // Constructor for DAO (includes all fields)
    public Book(String id, String title, String authors, byte[] coverImage, String description,
                String publishedDate, String publisher, boolean isFavorite,
                boolean intendingToRead, int currentPage) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.coverImage = coverImage;
        this.description = description;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.isFavorite = isFavorite;
        this.intendingToRead = intendingToRead;
        this.currentPage = currentPage;
    }

    // Constructor for API fetched books (without DB-specific fields)
    public Book(String id, String title, String authors, byte[] coverImage, String description,
                String publishedDate, String publisher) {
        this(id, title, authors, coverImage, description, publishedDate, publisher,
                false, false, 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isIntendingToRead() {
        return intendingToRead;
    }

    public void setIntendingToRead(boolean intendingToRead) {
        this.intendingToRead = intendingToRead;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", coverImage=" + Arrays.toString(coverImage) +
                ", description='" + description + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isFavorite=" + isFavorite +
                ", intendingToRead=" + intendingToRead +
                ", currentPage=" + currentPage +
                '}';
    }
}
