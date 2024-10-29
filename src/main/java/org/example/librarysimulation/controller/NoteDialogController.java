package org.example.librarysimulation.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.librarysimulation.model.Book;

public class NoteDialogController {

    @FXML
    private Label bookTitleLabel;

    @FXML
    private JFXTextField pageNumberField;

    @FXML
    private JFXTextArea noteTextArea;

    private Book book;

    /**
     * Sets the book for which the note is being added.
     *
     * @param book The book object.
     */
    public void setBook(Book book) {
        this.book = book;
        bookTitleLabel.setText("Book: " + book.getTitle());
    }

    /**
     * Retrieves the entered note content.
     *
     * @return The note content.
     */
    public String getNoteContent() {
        return noteTextArea.getText().trim();
    }

    /**
     * Retrieves the entered page number.
     *
     * @return The page number, or -1 if invalid.
     */
    public int getPageNumber() {
        String pageText = pageNumberField.getText().trim();
        try {
            return Integer.parseInt(pageText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
