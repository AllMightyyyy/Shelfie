package org.example.librarysimulation.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.tools.javac.Main;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.example.librarysimulation.MainApp;
import org.example.librarysimulation.dao.BookDAO;
import org.example.librarysimulation.model.Book;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksAPIUtils {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String API_KEY = "YOUR_API_KEY";
    private static byte[] placeholderImageBytes;

    static {
        try {
            Image placeholderImage = new Image(MainApp.class.getResource("images/book-cover-placeholder.png").toExternalForm());
            placeholderImageBytes = ImageUtil.imageToBytes(placeholderImage);
        } catch (Exception e) {
            e.printStackTrace();
            placeholderImageBytes = new byte[0];
        }
    }

    /**
     * Searches for books using Google Books API asynchronously.
     *
     * @param query    The search term, such as a title or author.
     * @param callback The callback to handle the list of books or errors.
     */
    public static void searchBooksAsync(String query, SearchCallback callback) {
        String url = BASE_URL + query + "&key=" + API_KEY;

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("Failed to fetch books: " + response.body());
                }

                return response.body();
            }
        };

        task.setOnSucceeded(event -> {
            String jsonResponse = task.getValue();
            try {
                List<Book> books = parseBooksResponse(jsonResponse);

                BookDAO bookDAO = new BookDAO();
                for (Book book : books) {
                    bookDAO.addBook(book);
                }
                callback.onSuccess(books);
            } catch (Exception e) {
                callback.onError(e);
            }
        });

        task.setOnFailed(event -> {
            callback.onError(task.getException());
        });

        new Thread(task).start();
    }

    /**
     * Parses the JSON response to extract book details.
     *
     * @param jsonResponse The JSON response from the API.
     * @return A list of Book objects.
     */
    public static List<Book> parseBooksResponse(String jsonResponse) {
        List<Book> books = new ArrayList<>();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        if (jsonObject.has("items")) {
            JsonArray items = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < items.size(); i++) {
                JsonObject volume = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = volume.getAsJsonObject("volumeInfo");

                String id = volume.has("id") ? volume.get("id").getAsString() : null;
                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No Title";

                String authors = "Unknown Author";
                if (volumeInfo.has("authors")) {
                    JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
                    StringBuilder authorsBuilder = new StringBuilder();
                    for (int j = 0; j < authorsArray.size(); j++) {
                        authorsBuilder.append(authorsArray.get(j).getAsString());
                        if (j < authorsArray.size() - 1) {
                            authorsBuilder.append(", ");
                        }
                    }
                    authors = authorsBuilder.toString();
                }

                String coverUrl = null;
                if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                    coverUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString().replace("http://", "https://");
                }

                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No Description Available.";
                String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Unknown Date";
                String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";

                Book book = new Book(id, title, authors, null, description, publishedDate, publisher);

                if (coverUrl != null) {
                    try {
                        Image coverImage = getCoverImageFromURL(coverUrl);
                        byte[] imageBytes = ImageUtil.imageToBytes(coverImage);
                        if (imageBytes != null && imageBytes.length > 0) {
                            book.setCoverImage(imageBytes);
                        } else {
                            book.setCoverImage(placeholderImageBytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        book.setCoverImage(placeholderImageBytes);
                    }
                } else {
                    book.setCoverImage(placeholderImageBytes);
                }

                books.add(book);
            }
        }

        return books;
    }

    /**
     * Downloads an Image from the provided URL.
     *
     * @param coverUrl The URL of the book cover.
     * @return The downloaded Image.
     */
    private static Image getCoverImageFromURL(String coverUrl) {
        if (coverUrl != null && !coverUrl.isEmpty()) {
            System.out.println("Loading image from URL: " + coverUrl);
            Image image = new Image(coverUrl, false);
            if (image.isError()) {
                System.out.println("Error loading image from URL: " + coverUrl);
            }
            return image;
        } else {
            System.out.println("Cover URL is null or empty, loading placeholder.");
            return new Image(MainApp.class.getResource("images/book-cover-placeholder.png").toExternalForm());
        }
    }

    /**
     * Fetches an Image for the book cover given the cover URL.
     *
     * @param coverUrl The URL of the book cover.
     * @return An Image object containing the book cover image.
     */
    public static Image getCoverImage(String coverUrl) {
        return getCoverImageFromURL(coverUrl);
    }

    /**
     * Callback interface for handling search results.
     */
    public interface SearchCallback {
        void onSuccess(List<Book> books);

        void onError(Throwable throwable);
    }
}
