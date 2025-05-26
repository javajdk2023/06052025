package br.com.libraryjdbc.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;


public class TestConstraints {

    public static void main(String[] args) {
        try {
            DB.getConnection();
            System.out.println("✅ Test connection established successfully!");

            createTablesIfNotExists();

            cleanTestData();

            testCategoryConstraints();
            testBookConstraints();

            System.out.println("\n✅ All constraint tests completed!");

        } catch (Exception e) {
            System.err.println("❌ Error in constraint tests: " + e.getMessage());
        } finally {
            DB.closeConnection();
            System.out.println("✅ Test connection closed successfully!");
        }
    }

    private static void testCategoryConstraints() {
        System.out.println("\n=== CATEGORY CONSTRAINTS TESTS ===");

        // Cenário 1: Inserção válida (fluxo principal)
        testValidCategoryInsertion();

        // Cenário 2: Nome duplicado (fluxo alternativo)
        testDuplicateCategoryName();

        // Cenário 3: Campo obrigatório nulo (fluxo de exceção)
        testCategoryNullFields();
    }

    private static void testBookConstraints() {
        System.out.println("\n=== BOOK CONSTRAINTS TESTS ===");

        // Cenário 1: Inserção válida (fluxo principal)
        testValidBookInsertion();

        // Cenário 2: ISBN duplicado e ano inválido (fluxo alternativo)
        testBookConstraintViolations();

        // Cenário 3: Categoria inexistente (fluxo de exceção)
        testBookInvalidForeignKey();
    }

    // === CATEGORY CONSTRAINT TESTS ===

    private static void testValidCategoryInsertion() {
        System.out.println("\n--- Valid Category Insertion ---");

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Test Category");
                st.setString(2, "Test Description");

                int rows = st.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Valid category inserted successfully");
                } else {
                    System.out.println("❌ Failed to insert valid category");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Unexpected error in valid insertion: " + e.getMessage());
        }
    }

    private static void testDuplicateCategoryName() {
        System.out.println("\n--- Duplicate Category Name Test ---");

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Test Category"); // Same name as before
                st.setString(2, "Different Description");

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: Duplicate name was allowed!");

            }

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                System.out.println("✅ UNIQUE constraint on category name working: " + e.getMessage());
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void testCategoryNullFields() {
        System.out.println("\n--- Category NULL Fields Test ---");

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, null);
                st.setString(2, "Description");

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: NULL name was allowed!");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("null") || e.getMessage().contains("NOT NULL")) {
                System.out.println("✅ NOT NULL constraint on category name working");
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Valid Name");
                st.setString(2, null);

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: NULL description was allowed!");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("null") || e.getMessage().contains("NOT NULL")) {
                System.out.println("✅ NOT NULL constraint on category description working");
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }
    }

    // === BOOK CONSTRAINT TESTS ===

    private static void testValidBookInsertion() {
        System.out.println("\n--- Valid Book Insertion ---");

        try {
            Long categoryId = insertTestCategory();

            Connection conn = DB.getConnection();
            String sql = "INSERT INTO book (title, author, synopsis, isbn, release_year, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Test Book");
                st.setString(2, "Test Author");
                st.setString(3, "Test Synopsis");
                st.setString(4, "1234567890123");
                st.setInt(5, 2000);
                st.setLong(6, categoryId);

                int rows = st.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Valid book inserted successfully");
                } else {
                    System.out.println("❌ Failed to insert valid book");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Unexpected error in valid book insertion: " + e.getMessage());
        }
    }

    private static void testBookConstraintViolations() {
        System.out.println("\n--- Book Constraint Violations Test ---");

        Long categoryId = insertTestCategory();

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO book (title, author, synopsis, isbn, release_year, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Another Book");
                st.setString(2, "Another Author");
                st.setString(3, "Another Synopsis");
                st.setString(4, "1234567890123"); // Same ISBN as before
                st.setInt(5, 2010);
                st.setLong(6, categoryId);

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: Duplicate ISBN was allowed!");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                System.out.println("✅ UNIQUE constraint on ISBN working");
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO book (title, author, synopsis, isbn, release_year, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Old Book");
                st.setString(2, "Old Author");
                st.setString(3, "Old Synopsis");
                st.setString(4, "9999999999999");
                st.setInt(5, 1950); // Invalid year
                st.setLong(6, categoryId);

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: Invalid year (< 1967) was allowed!");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("check") || e.getMessage().contains("release_year")) {
                System.out.println("✅ CHECK constraint on release_year >= 1967 working");
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void testBookInvalidForeignKey() {
        System.out.println("\n--- Book Invalid Foreign Key Test ---");

        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO book (title, author, synopsis, isbn, release_year, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Test Book");
                st.setString(2, "Test Author");
                st.setString(3, "Test Synopsis");
                st.setString(4, "8888888888888");
                st.setInt(5, 2020);
                st.setLong(6, 999999L); // Invalid category_id

                st.executeUpdate();
                System.out.println("❌ CONSTRAINT FAILURE: Invalid foreign key was allowed!");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign") || e.getMessage().contains("reference")) {
                System.out.println("✅ FOREIGN KEY constraint on category_id working");
            } else {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }
    }

    // === HELPER METHODS ===

    private static void createTablesIfNotExists() {
        try {
            Connection conn = DB.getConnection();

            try (PreparedStatement st = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS category (" +
                            "id SERIAL PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL UNIQUE," +
                            "description TEXT NOT NULL" +
                            ")")) {
                st.executeUpdate();
            }

            try (PreparedStatement st = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS book (" +
                            "id SERIAL PRIMARY KEY," +
                            "title VARCHAR(200) NOT NULL," +
                            "author VARCHAR(150) NOT NULL," +
                            "synopsis TEXT," +
                            "isbn VARCHAR(20) NOT NULL UNIQUE," +
                            "release_year INTEGER NOT NULL CHECK (release_year >= 1967)," +
                            "category_id INTEGER NOT NULL," +
                            "FOREIGN KEY (category_id) REFERENCES category(id)" +
                            ")")) {
                st.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DbException("Error creating tables: " + e.getMessage());
        }
    }

    private static void cleanTestData() {
        try {
            Connection conn = DB.getConnection();

            try (PreparedStatement st = conn.prepareStatement("DELETE FROM book WHERE isbn LIKE '%test%' OR isbn LIKE '%123%' OR isbn LIKE '%999%' OR isbn LIKE '%888%'")) {
                st.executeUpdate();
            }

            try (PreparedStatement st = conn.prepareStatement("DELETE FROM category WHERE name LIKE '%Test%'")) {
                st.executeUpdate();
            }

        } catch (SQLException e) {
        }
    }

    private static Long insertTestCategory() {
        try {
            Connection conn = DB.getConnection();
            String sql = "INSERT INTO category (name, description) VALUES (?, ?) RETURNING id";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, "Test Category " + System.currentTimeMillis());
                st.setString(2, "Test Description");

                var rs = st.executeQuery();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            try {
                Connection conn = DB.getConnection();
                String sql = "SELECT id FROM category LIMIT 1";

                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    var rs = st.executeQuery();
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            } catch (SQLException e2) {
                throw new DbException("Cannot create or find test category");
            }
        }

        throw new DbException("Cannot create test category");
    }
}