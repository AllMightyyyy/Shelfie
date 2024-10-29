package org.example.librarysimulation.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarysimulation.MainApp;
import org.example.librarysimulation.dao.BookDAO;
import org.example.librarysimulation.dao.NoteDAO;
import org.example.librarysimulation.model.Book;
import org.example.librarysimulation.model.Note;
import org.example.librarysimulation.util.GoogleBooksAPIUtils;
import org.example.librarysimulation.util.ImageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXComboBox<String> genreComboBox;

    @FXML
    private JFXComboBox<String> yearComboBox;

    @FXML
    private VBox bookListView;

    @FXML
    private ImageView bookCoverImage;

    @FXML
    private Label bookTitle;

    @FXML
    private Label bookAuthor;

    @FXML
    private Label bookPublishedDate;

    @FXML
    private Label bookPublisher;

    @FXML
    private TextArea bookDescription;

    @FXML
    private JFXTextField currentPageField;

    @FXML
    private JFXButton favoriteButtonDetails;

    @FXML
    private JFXButton intendingToReadButtonDetails;

    @FXML
    private JFXButton addNoteButtonDetails;

    @FXML
    private JFXButton viewNotesButton;

    @FXML
    private ToggleButton offlineToggle;

    private BookDAO bookDAO;
    private NoteDAO noteDAO;

    private boolean isOfflineMode = false;

    private Book currentSelectedBook = null;

    @FXML
    public void initialize() {
        try {
            bookDAO = new BookDAO();
            noteDAO = new NoteDAO();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize DAOs.");
            return;
        }

        genreComboBox.getItems().addAll("All", "Fiction", "Non-Fiction", "Science", "Fantasy", "History", "Biography", "Mystery");
        genreComboBox.getSelectionModel().selectFirst();

        int currentYear = java.time.Year.now().getValue();
        for (int year = 1900; year <= currentYear; year++) {
            yearComboBox.getItems().add(String.valueOf(year));
        }
        yearComboBox.getItems().add(0, "Any");
        yearComboBox.getSelectionModel().selectFirst();

        loadBooksFromDatabase();
    }

    /**
     * Handles the search action triggered by the user.
     */
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        String genre = genreComboBox.getValue();
        String year = yearComboBox.getValue();

        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a search term.");
            return;
        }

        StringBuilder searchQuery = new StringBuilder(query);
        if (!"All".equalsIgnoreCase(genre) && !"Any".equalsIgnoreCase(year)) {
            searchQuery.append("+subject:").append(genre);
            searchQuery.append("+publishedDate:").append(year);
        } else if (!"All".equalsIgnoreCase(genre)) {
            searchQuery.append("+subject:").append(genre);
        } else if (!"Any".equalsIgnoreCase(year)) {
            searchQuery.append("+publishedDate:").append(year);
        }

        if (isOfflineMode) {
            try {
                List<Book> books = bookDAO.searchBooks(searchQuery.toString());
                displayBooks(books);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Search Error", "Failed to search books in the database.");
            }
        } else {
            GoogleBooksAPIUtils.searchBooksAsync(searchQuery.toString(), new GoogleBooksAPIUtils.SearchCallback() {
                @Override
                public void onSuccess(List<Book> books) {
                    Platform.runLater(() -> {
                        displayBooks(books);
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, "Search Error", throwable.getMessage());
                    });
                }
            });
        }
    }

    /**
     * Displays the list of books in the bookListView.
     *
     * @param books The list of books to display.
     */
    private void displayBooks(List<Book> books) {
        bookListView.getChildren().clear();

        if (books.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No books found for the given search criteria.");
            return;
        }

        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("fxml/BookItem.fxml"));
                HBox bookItem = loader.load();

                BookItemController controller = loader.getController();
                controller.setBook(book);
                controller.setMainController(this);

                bookListView.getChildren().add(bookItem);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "UI Error", "Failed to load book item.");
            }
        }
    }

    /**
     * Displays detailed information about the selected book.
     *
     * @param book The selected book.
     */
    public void displayBookDetails(Book book) {
        if (book != null) {
            currentSelectedBook = book;

            bookTitle.setText(book.getTitle());
            bookAuthor.setText("By: " + book.getAuthors());
            bookPublishedDate.setText("Published: " + book.getPublishedDate());
            bookPublisher.setText("Publisher: " + book.getPublisher());
            bookDescription.setText(book.getDescription());

            Image coverImage = ImageUtil.bytesToImage(book.getCoverImage());
            if (coverImage == null) {
                coverImage = new Image(getClass().getResource("/images/book-cover-placeholder.png").toExternalForm());
            }
            bookCoverImage.setImage(coverImage);

            currentPageField.setText(String.valueOf(book.getCurrentPage()));

            updateFavoriteButton();
            updateIntendingToReadButton();
        }
    }

    /**
     * Updates the favorite button appearance based on the book's favorite status.
     */
    private void updateFavoriteButton() {
        if (currentSelectedBook == null) return;

        if (currentSelectedBook.isFavorite()) {
            favoriteButtonDetails.setText("★ Favorite");
        } else {
            favoriteButtonDetails.setText("☆ Favorite");
        }
    }

    /**
     * Updates the intending-to-read button appearance based on the book's status.
     */
    private void updateIntendingToReadButton() {
        if (currentSelectedBook == null) return;

        if (currentSelectedBook.isIntendingToRead()) {
            intendingToReadButtonDetails.setText("📚 Intend to Read");
        } else {
            intendingToReadButtonDetails.setText("📖 Intend to Read");
        }
    }

    /**
     * Handles the exit action.
     */
    @FXML
    private void handleExit() {
        Platform.exit();
    }

    /**
     * Handles the about action.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Book Library Management");
        alert.setContentText("Version 1.0\nDeveloped by Zakaria Farih.\n© 2024 with love.");
        alert.showAndWait();
    }

    /**
     * Utility method to show alerts.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert.
     * @param message The message content.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Marks or unmarks a book as favorite.
     *
     * @param bookId     The ID of the book.
     * @param isFavorite The new favorite status.
     */
    public void markAsFavorite(String bookId, boolean isFavorite) {
        bookDAO.setFavorite(bookId, isFavorite);
        if (currentSelectedBook != null && currentSelectedBook.getId().equals(bookId)) {
            currentSelectedBook.setFavorite(isFavorite);
            updateFavoriteButton();
        }
        refreshBookList();
    }

    /**
     * Marks or unmarks a book as intending to read.
     *
     * @param bookId            The ID of the book.
     * @param intendingToRead    The new intending-to-read status.
     */
    public void markAsIntendingToRead(String bookId, boolean intendingToRead) {
        bookDAO.setIntendingToRead(bookId, intendingToRead);
        if (currentSelectedBook != null && currentSelectedBook.getId().equals(bookId)) {
            currentSelectedBook.setIntendingToRead(intendingToRead);
            updateIntendingToReadButton();
        }
        refreshBookList();
    }

    /**
     * Updates the current reading page of the selected book.
     */
    @FXML
    private void handleUpdatePage() {
        if (currentSelectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to update the reading progress.");
            return;
        }

        String pageText = currentPageField.getText().trim();
        if (pageText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter the current page number.");
            return;
        }

        try {
            int currentPage = Integer.parseInt(pageText);
            if (currentPage < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Page number cannot be negative.");
                return;
            }
            bookDAO.updateCurrentPage(currentSelectedBook.getId(), currentPage);
            currentSelectedBook.setCurrentPage(currentPage);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reading progress updated.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid page number.");
        }
    }

    /**
     * Handles the favorite button in the book details pane.
     */
    @FXML
    private void handleFavoriteDetails() {
        if (currentSelectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to mark as favorite.");
            return;
        }

        boolean newStatus = !currentSelectedBook.isFavorite();
        markAsFavorite(currentSelectedBook.getId(), newStatus);
    }

    /**
     * Handles the intending-to-read button in the book details pane.
     */
    @FXML
    private void handleIntendingToReadDetails() {
        if (currentSelectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to add to intending-to-read.");
            return;
        }

        boolean newStatus = !currentSelectedBook.isIntendingToRead();
        markAsIntendingToRead(currentSelectedBook.getId(), newStatus);
    }

    /**
     * Handles the add note button in the book details pane.
     */
    @FXML
    private void handleAddNoteDetails() {
        if (currentSelectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to add a note.");
            return;
        }

        showAddNoteDialog(currentSelectedBook);
    }

    /**
     * Handles the view notes button in the book details pane.
     */
    @FXML
    private void handleViewNotes() {
        if (currentSelectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to view notes.");
            return;
        }

        try {
            showViewNotesDialog(currentSelectedBook);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open view notes dialog.");
        }
    }

    /**
     * Shows a dialog to add a note to the specified book.
     *
     * @param book The book to add a note to.
     */
    public void showAddNoteDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("fxml/NoteDialog.fxml"));
            DialogPane dialogPane = loader.load();

            NoteDialogController controller = loader.getController();
            controller.setBook(book);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Note");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                String noteContent = controller.getNoteContent();
                int pageNumber = controller.getPageNumber();

                if (noteContent.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a note.");
                    return;
                }

                if (pageNumber <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid page number.");
                    return;
                }

                Note note = new Note(book.getId(), pageNumber, noteContent);
                try {
                    noteDAO.addNote(note);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Note added successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add note.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add note dialog.");
        }
    }

    /**
     * Shows a dialog to view notes for the specified book.
     *
     * @param book The book whose notes are to be viewed.
     */
    public void showViewNotesDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("fxml/ViewNotesDialog.fxml"));
            DialogPane dialogPane = loader.load();

            ViewNotesDialogController controller = loader.getController();
            controller.setBook(book);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("View Notes");

            dialog.showAndWait();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open view notes dialog.");
        }
    }

    /**
     * Retrieves the ID of the currently selected book.
     *
     * @return The book ID or null if no book is selected.
     */
    private String getCurrentBookId() {
        if (currentSelectedBook != null) {
            return currentSelectedBook.getId();
        }
        return null;
    }

    /**
     * Loads all books from the database to display in offline mode.
     */
    private void loadBooksFromDatabase() {
        List<Book> books = bookDAO.getAllBooks();
        displayBooks(books);
    }

    /**
     * Refreshes the book list view to reflect any changes.
     */
    private void refreshBookList() {
        handleSearch();
    }

    /**
     * Handles the toggle between online and offline modes.
     */
    @FXML
    private void handleToggleOffline() {
        isOfflineMode = offlineToggle.isSelected();
        if (isOfflineMode) {
            offlineToggle.setText("ON");
            showAlert(Alert.AlertType.INFORMATION, "Offline Mode", "You are now in Offline Mode.");
            loadBooksFromDatabase();
        } else {
            offlineToggle.setText("OFF");
            showAlert(Alert.AlertType.INFORMATION, "Online Mode", "You are now in Online Mode.");
        }
    }

    /**
     * Handles the action to view all books.
     */
    @FXML
    private void handleViewAllBooks() {
        if (isOfflineMode) {
            loadBooksFromDatabase();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "View All Books", "In Online Mode, perform a search to view books.");
        }
    }

    /**
     * Handles the action to view favorite books.
     */
    @FXML
    private void handleViewFavorites() {
        if (isOfflineMode) {
            try {
                List<Book> favoriteBooks = bookDAO.getFavoriteBooks();
                displayBooks(favoriteBooks);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve favorite books.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "View Favorites", "Favorites are managed in Offline Mode.");
        }
    }

    /**
     * Handles the action to view books intending to read.
     */
    @FXML
    private void handleViewIntendingToRead() {
        if (isOfflineMode) {
            try {
                List<Book> intendingToReadBooks = bookDAO.getIntendingToReadBooks();
                displayBooks(intendingToReadBooks);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve intending to read books.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "View Intending to Read", "Intending to Read is managed in Offline Mode.");
        }
    }

    /**
     * Handles the action to view books that are both favorite and intending to read.
     */
    @FXML
    private void handleViewFavoritesAndIntendingToRead() {
        if (isOfflineMode) {
            try {
                List<Book> filteredBooks = bookDAO.getFavoriteAndIntendingToReadBooks();
                displayBooks(filteredBooks);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve filtered books.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "View Favorites & Intending to Read", "This filter is available in Offline Mode.");
        }
    }
}
