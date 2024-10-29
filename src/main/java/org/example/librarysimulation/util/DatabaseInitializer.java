package org.example.librarysimulation.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DatabaseUtility.getInstance().getConnection()) {
            if (conn != null) {
                initializeBooksTable(conn);
                initializeNotesTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeBooksTable(Connection conn) throws SQLException {
        if (!tableExists(conn, "books")) {
            String createBooksTableSQL = """
                    CREATE TABLE books (
                        id VARCHAR(255) PRIMARY KEY,
                        title VARCHAR(255),
                        authors VARCHAR(255),
                        cover_image BLOB,
                        description TEXT,
                        published_date VARCHAR(50),
                        publisher VARCHAR(255),
                        is_favorite BOOLEAN DEFAULT FALSE,
                        intending_to_read BOOLEAN DEFAULT FALSE,
                        current_page INT DEFAULT 0
                    );
                    """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createBooksTableSQL);
                System.out.println("Created 'books' table.");
            }
        } else {
            addColumnIfNotExists(conn, "books", "is_favorite", "BOOLEAN DEFAULT FALSE");
            addColumnIfNotExists(conn, "books", "intending_to_read", "BOOLEAN DEFAULT FALSE");
            addColumnIfNotExists(conn, "books", "current_page", "INT DEFAULT 0");
        }
    }

    private static void initializeNotesTable(Connection conn) throws SQLException {
        // Check if the notes table exists
        if (!tableExists(conn, "notes")) {
            String createNotesTableSQL = """
                    CREATE TABLE notes (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        book_id VARCHAR(255),
                        page_number INT,
                        note TEXT,
                        FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
                    );
                    """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createNotesTableSQL);
                System.out.println("Created 'notes' table.");
            }
        }
    }

    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static void addColumnIfNotExists(Connection conn, String tableName, String columnName, String columnDefinition) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, columnName)) {
            if (!rs.next()) {
                String addColumnSQL = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition + ";";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(addColumnSQL);
                    System.out.println("Added column '" + columnName + "' to '" + tableName + "' table.");
                }
            }
        }
    }
}
