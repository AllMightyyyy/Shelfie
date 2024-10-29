package org.example.librarysimulation.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.librarysimulation.MainApp;
import org.example.librarysimulation.model.Book;
import org.example.librarysimulation.util.ImageUtil;

public class BookItemController {

    @FXML
    private ImageView coverImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private JFXButton viewButton;

    @FXML
    private JFXButton favoriteButton;

    @FXML
    private JFXButton intendingToReadButton;

    @FXML
    private JFXButton addNoteButton;

    private Book book;

    private MainController mainController;

    /**
     * Sets the book data and updates the UI components.
     *
     * @param book The book to display.
     */
    public void setBook(Book book) {
        this.book = book;
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthors());

        byte[] coverImageBytes = book.getCoverImage();
        if (coverImageBytes != null) {
            coverImageView.setImage(ImageUtil.bytesToImage(coverImageBytes));
        } else {
            coverImageView.setImage(new javafx.scene.image.Image(MainApp.class.getResource("images/placeholder.png").toExternalForm()));
        }

        updateFavoriteButton();
        updateIntendingToReadButton();
    }

    /**
     * Updates the favorite button appearance based on the book's favorite status.
     */
    private void updateFavoriteButton() {
        if (book.isFavorite()) {
            favoriteButton.setText("★");
        } else {
            favoriteButton.setText("☆");
        }
    }

    /**
     * Updates the intending-to-read button appearance based on the book's status.
     */
    private void updateIntendingToReadButton() {
        if (book.isIntendingToRead()) {
            intendingToReadButton.setText("📚");
        } else {
            intendingToReadButton.setText("📖");
        }
    }

    /**
     * Sets the main controller to allow communication.
     *
     * @param mainController The main controller.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        // No additional initialization required here
    }

    @FXML
    private void handleView() {
        if (mainController != null && book != null) {
            mainController.displayBookDetails(book);
        }
    }

    @FXML
    private void handleFavorite() {
        if (mainController != null && book != null) {
            boolean newStatus = !book.isFavorite();
            book.setFavorite(newStatus);
            mainController.markAsFavorite(book.getId(), newStatus);
            updateFavoriteButton();
        }
    }

    @FXML
    private void handleIntendingToRead() {
        if (mainController != null && book != null) {
            boolean newStatus = !book.isIntendingToRead();
            book.setIntendingToRead(newStatus);
            mainController.markAsIntendingToRead(book.getId(), newStatus);
            updateIntendingToReadButton();
        }
    }

    @FXML
    private void handleAddNote() {
        if (mainController != null && book != null) {
            mainController.showAddNoteDialog(book);
        }
    }
}
