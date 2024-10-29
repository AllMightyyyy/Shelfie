package org.example.librarysimulation.dao;

import org.example.librarysimulation.model.Book;
import org.example.librarysimulation.util.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(Book book) {
        // Add the book to the database
        String sql = "INSERT INTO books (id, title, authors, cover_image, description, published_date, publisher, is_favorite, intending_to_read, current_page) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "title = VALUES(title), " +
                "authors = VALUES(authors), " +
                "cover_image = VALUES(cover_image), " +
                "description = VALUES(description), " +
                "published_date = VALUES(published_date), " +
                "publisher = VALUES(publisher);";
        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthors());
            pstmt.setBytes(4, book.getCoverImage());
            pstmt.setString(5, book.getDescription());
            pstmt.setString(6, book.getPublishedDate());
            pstmt.setString(7, book.getPublisher());
            pstmt.setBoolean(8, book.isFavorite());
            pstmt.setBoolean(9, book.isIntendingToRead());
            pstmt.setInt(10, book.getCurrentPage());
            pstmt.executeUpdate();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        // Retrieve all books from the database
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try(Connection conn = DatabaseUtility.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        ) {
           while(rs.next()) {
               Book book = new Book(
                       rs.getString("id"),
                       rs.getString("title"),
                       rs.getString("authors"),
                       rs.getBytes("cover_image"),
                       rs.getString("description"),
                       rs.getString("published_date"),
                       rs.getString("publisher"),
                       rs.getBoolean("is_favorite"),
                       rs.getBoolean("intending_to_read"),
                       rs.getInt("current_page")
               );
               books.add(book);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookById(String id) {
        // Retrieve a book by its ID
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = null;
        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("authors"),
                            rs.getBytes("cover_image"),
                            rs.getString("description"),
                            rs.getString("published_date"),
                            rs.getString("publisher"),
                            rs.getBoolean("is_favorite"),
                            rs.getBoolean("intending_to_read"),
                            rs.getInt("current_page")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, authors = ?, cover_image = ?, description = ?, " +
                "published_date = ?, publisher = ?, is_favorite = ?, intending_to_read = ?, current_page = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthors());
            pstmt.setBytes(3, book.getCoverImage());
            pstmt.setString(4, book.getDescription());
            pstmt.setString(5, book.getPublishedDate());
            pstmt.setString(6, book.getPublisher());
            pstmt.setBoolean(7, book.isFavorite());
            pstmt.setBoolean(8, book.isIntendingToRead());
            pstmt.setInt(9, book.getCurrentPage());
            pstmt.setString(10, book.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(String id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFavorite(String id, boolean isFavorite) {
        // Set the favorite status of a book
        String sql = "UPDATE books SET is_favorite = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, isFavorite);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIntendingToRead(String id, boolean intendingToRead) {
        String sql = "UPDATE books SET intending_to_read = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, intendingToRead);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentPage(String id, int currentPage) {
        String sql = "UPDATE books SET current_page = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentPage);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR authors LIKE ? OR description LIKE ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("authors"),
                            rs.getBytes("cover_image"),
                            rs.getString("description"),
                            rs.getString("published_date"),
                            rs.getString("publisher"),
                            rs.getBoolean("is_favorite"),
                            rs.getBoolean("intending_to_read"),
                            rs.getInt("current_page")
                    );
                    books.add(book);
                }
            }
        }
        return books;
    }

    /**
     * Retrieves all favorite books from the database.
     *
     * @return A list of favorite books.
     */
    public List<Book> getFavoriteBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_favorite = TRUE";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getBytes("cover_image"),
                        rs.getString("description"),
                        rs.getString("published_date"),
                        rs.getString("publisher"),
                        rs.getBoolean("is_favorite"),
                        rs.getBoolean("intending_to_read"),
                        rs.getInt("current_page")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Retrieves all books marked as intending to read from the database.
     *
     * @return A list of books intending to be read.
     */
    public List<Book> getIntendingToReadBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE intending_to_read = TRUE";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getBytes("cover_image"),
                        rs.getString("description"),
                        rs.getString("published_date"),
                        rs.getString("publisher"),
                        rs.getBoolean("is_favorite"),
                        rs.getBoolean("intending_to_read"),
                        rs.getInt("current_page")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Retrieves all favorite books that are also marked as intending to read.
     *
     * @return A list of books that are both favorite and intending to read.
     */
    public List<Book> getFavoriteAndIntendingToReadBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_favorite = TRUE AND intending_to_read = TRUE";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getBytes("cover_image"),
                        rs.getString("description"),
                        rs.getString("published_date"),
                        rs.getString("publisher"),
                        rs.getBoolean("is_favorite"),
                        rs.getBoolean("intending_to_read"),
                        rs.getInt("current_page")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
