package org.example.librarysimulation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import org.example.librarysimulation.dao.NoteDAO;
import org.example.librarysimulation.model.Book;
import org.example.librarysimulation.model.Note;

import java.sql.SQLException;
import java.util.List;

public class ViewNotesDialogController {

    @FXML
    private Label bookTitleLabel;

    @FXML
    private ListView<String> notesListView;

    private Book book;
    private NoteDAO noteDAO;

    /**
     * Sets the book whose notes are to be viewed.
     *
     * @param book The book object.
     */
    public void setBook(Book book) throws SQLException {
        this.book = book;
        bookTitleLabel.setText("Notes for: " + book.getTitle());
        noteDAO = new NoteDAO();
        loadNotes();
    }

    /**
     * Loads notes from the database and displays them in the ListView.
     */
    private void loadNotes() {
        List<Note> notes = noteDAO.getNotesByBookId(book.getId());
        notesListView.getItems().clear();

        if (notes.isEmpty()) {
            notesListView.getItems().add("No notes available.");
            return;
        }

        for (Note note : notes) {
            notesListView.getItems().add("Page " + note.getPageNumber() + ": " + note.getNote());
        }
    }
}
