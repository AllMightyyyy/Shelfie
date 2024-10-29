
# 📚 Shelfie

**Shelfie** is a feature-rich library management application built with JavaFX. It allows users to search for books using the Google Books API, manage their personal library, mark favorites, add notes, and track their reading progress. The app seamlessly integrates with a MySQL database to store and retrieve book information, ensuring a smooth and efficient user experience both online and offline.

---

## 🚀 Features

- **Search Books:** Utilize the Google Books API to find books by title, author, genre, or publication year.
- **Manage Library:** Add books to your personal library, mark favorites, and indicate books you intend to read.
- **Reading Progress:** Track your current reading page for each book.
- **Add Notes:** Attach notes to specific pages of your books for easy reference.
- **Offline Mode:** Access and manage your library without an internet connection.
- **User-Friendly Interface:** Intuitive and responsive UI built with JavaFX and JFoenix controls.

---

## 🛠️ Technologies Used

- **JavaFX:** For building the graphical user interface.
- **JFoenix:** Material Design components for JavaFX.
- **MySQL:** Database for storing book and note information.
- **HikariCP:** High-performance JDBC connection pooling.
- **Google Books API:** Fetch book data and covers.
- **Gson:** JSON parsing for handling API responses.

---

## 📸 Screenshots

<!-- 
**Add screenshots of your application here. Replace the placeholders with actual images.**

![Main Interface](path/to/main_interface.png)
![Book Details](path/to/book_details.png)
![Add Note](path/to/add_note.png)
-->

*Please add screenshots of the application's main interface, book details view, add note dialog, and any other significant features to showcase the user interface and functionality.*

---

## 📥 Installation

### Prerequisites

- **Java Development Kit (JDK) 11 or higher**
- **MySQL Server**
- **Maven** (for dependency management)
- **Internet Connection** (for fetching data via Google Books API)

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/Shelfie.git
   cd Shelfie
   ```

2. **Set Up the MySQL Database**

   - **Create Database:**

     ```sql
     CREATE DATABASE booklibrary;
     ```

   - **Update Database Credentials:**

     Modify the `DatabaseUtility` class located at `src/main/java/org/example/librarysimulation/util/DatabaseUtility.java` with your MySQL username and password.

     ```java
     private static final String USER = "your_mysql_username";
     private static final String PASSWORD = "your_mysql_password";
     ```

   - **Initialize Tables:**

     The application automatically initializes the required tables (`books` and `notes`) upon the first run.

3. **Build the Project**

   Ensure you have Maven installed. Navigate to the project directory and run:

   ```bash
   mvn clean install
   ```

4. **Run the Application**

   Execute the `MainApp` class located at `src/main/java/org/example/librarysimulation/MainApp.java`.

   ```bash
   mvn javafx:run
   ```

---

## 🖥️ Usage

1. **Launching the App:**

   Upon launching, you'll see the main interface where you can search for books.

2. **Searching for Books:**

   - Enter a search term (title, author, etc.) in the search field.
   - Optionally, filter by genre and publication year using the dropdowns.
   - Click the **Search** button to fetch results from the Google Books API or your local database if in offline mode.

3. **Managing Your Library:**

   - **View Details:** Click the **View** button on a book item to see detailed information.
   - **Mark as Favorite:** Toggle the favorite status by clicking the **Favorite** button (★/☆).
   - **Intend to Read:** Indicate your intention to read a book by clicking the **Intend to Read** button (📚/📖).
   - **Add Notes:** Attach notes to specific pages using the **Add Note** button.

4. **Offline Mode:**

   - Toggle the **Offline Mode** switch to access your library without internet connectivity.
   - In offline mode, you can manage your library using the data stored in the local MySQL database.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps to contribute:

1. **Fork the Repository**

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/YourFeature
   ```

3. **Commit Your Changes**

   ```bash
   git commit -m "Add some feature"
   ```

4. **Push to the Branch**

   ```bash
   git push origin feature/YourFeature
   ```

5. **Open a Pull Request**

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📬 Contact

**Your Name** – [your.email@example.com](mailto:your.email@example.com)

**Project Link:** [https://github.com/yourusername/Shelfie](https://github.com/yourusername/Shelfie)

---

## 🔧 Configuration Details

### Google Books API

- **API Key:** Ensure you have a valid Google Books API key. Replace the placeholder in `GoogleBooksAPIUtils` class with your actual API key.

  ```java
  private static final String API_KEY = "YOUR_GOOGLE_BOOKS_API_KEY";
  ```

### Database Configuration

- **Database URL:** Modify the `URL` in `DatabaseUtility` if your MySQL server is hosted elsewhere or uses a different port.

  ```java
  private static final String URL = "jdbc:mysql://localhost:3306/booklibrary?useSSL=false&serverTimezone=UTC";
  ```

---

## 🧩 Project Structure

```
Shelfie/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/librarysimulation/
│   │   │       ├── controller/
│   │   │       ├── dao/
│   │   │       ├── model/
│   │   │       ├── util/
│   │   │       └── MainApp.java
│   │   └── resources/
│   │       └── fxml/
│   │           ├── MainLayout.fxml
│   │           ├── BookItem.fxml
│   │           ├── NoteDialog.fxml
│   │           └── ViewNotesDialog.fxml
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

---

## 📝 Additional Notes

- **Error Handling:** The application includes basic error handling for database connections and API requests. Ensure that your MySQL server is running and accessible.
- **Dependencies:** All necessary dependencies are managed via Maven. Refer to the `pom.xml` file for more details.
- **Future Enhancements:** Consider adding user authentication, more detailed book analytics, and enhanced UI features.

---

Happy Reading! 📖✨
