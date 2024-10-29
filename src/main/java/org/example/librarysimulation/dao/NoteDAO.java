package org.example.librarysimulation.dao;

import org.example.librarysimulation.model.Note;
import org.example.librarysimulation.util.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public void addNote(Note note) {
        String sql = "INSERT INTO notes (book_id, page_number, note) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, note.getBookId());
            pstmt.setInt(2, note.getPageNumber());
            pstmt.setString(3, note.getNote());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Note added successfully for book ID: " + note.getBookId());
            } else {
                System.err.println("Failed to add note for book ID: " + note.getBookId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException occurred while adding a note.");
        }
    }

    public List<Note> getNotesByBookId(String bookId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE book_id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Note note = new Note(
                            rs.getInt("id"),
                            rs.getString("book_id"),
                            rs.getInt("page_number"),
                            rs.getString("note")
                    );
                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public void updateNote(Note note) {
        String sql = "UPDATE notes SET page_number = ?, note = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, note.getPageNumber());
            pstmt.setString(2, note.getNote());
            pstmt.setInt(3, note.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = DatabaseUtility.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
