package br.com.libraryjdbc.app;

import java.sql.Connection;
import java.util.List;

import br.com.libraryjdbc.dao.BookDao;
import br.com.libraryjdbc.dao.CategoryDao;
import br.com.libraryjdbc.model.Book;
import br.com.libraryjdbc.model.Category;
import db.DB;

public class Program {

    public static void main(String[] args) {
        try {
            Connection conn = DB.getConnection();
            System.out.println("✅ PostgreSQL connection established successfully!");

            // Test connection validity
            boolean valid = conn.isValid(5);
            System.out.println("✅ Connection is valid? " + valid);

            System.out.println("🚀 Library Management System");

            CategoryDao categoryDao = new CategoryDao();
            BookDao bookDao = new BookDao();

            // Create tables
            categoryDao.createTable();
            bookDao.createTable();

            System.out.println("✅ Tables created successfully!");

            // Test category operations
            System.out.println("\n📚 Testing category operations...");

            try {
                // Creating and saving categories
                Category fiction = new Category("Fiction", "Fiction books including novels and short stories");
                Category technical = new Category("Technical", "Technical and programming books");
                Category scienceFiction = new Category("Science Fiction", "Science fiction and fantasy books");

                categoryDao.save(fiction);
                categoryDao.save(technical);
                categoryDao.save(scienceFiction);

                System.out.println("✅ Sample categories inserted!");

                // List all categories
                System.out.println("\n📋 Categories list:");
                List<Category> categories = categoryDao.findAll();
                categories.forEach(System.out::println);

                // Test book operations
                System.out.println("\n📖 Testing book operations...");

                // Create sample books
                Book cleanCode = new Book("Clean Code", "Robert C. Martin",
                        "A handbook of agile software craftsmanship", "9780132350884", 2008, technical);

                Book effectiveJava = new Book("Effective Java", "Joshua Bloch",
                        "Best practices for Java programming", "9780134685991", 2017, technical);

                Book dune = new Book("Dune", "Frank Herbert",
                        "Epic science fiction novel", "9780441172719", 1965, scienceFiction);

                Book nineteenEightyFour = new Book("1984", "George Orwell",
                        "Dystopian social science fiction novel", "9780451524935", 1949, fiction);

                // Save books (some should fail validation)
                try {
                    bookDao.save(cleanCode);
                    System.out.println("✅ Clean Code saved successfully!");
                } catch (Exception e) {
                    System.out.println("❌ Error saving Clean Code: " + e.getMessage());
                }

                try {
                    bookDao.save(effectiveJava);
                    System.out.println("✅ Effective Java saved successfully!");
                } catch (Exception e) {
                    System.out.println("❌ Error saving Effective Java: " + e.getMessage());
                }

                try {
                    bookDao.save(dune);
                    System.out.println("❌ Dune should fail (year < 1967)");
                } catch (Exception e) {
                    System.out.println("✅ Dune validation working: " + e.getMessage());
                }

                try {
                    bookDao.save(nineteenEightyFour);
                    System.out.println("❌ 1984 should fail (year < 1967)");
                } catch (Exception e) {
                    System.out.println("✅ 1984 validation working: " + e.getMessage());
                }

                // Fix years and try again
                dune.setReleaseYear(1967); // Minimum valid year
                nineteenEightyFour.setReleaseYear(1967); // Fix year for demo

                try {
                    bookDao.save(dune);
                    System.out.println("✅ Dune saved with corrected year!");
                } catch (Exception e) {
                    System.out.println("❌ Error saving corrected Dune: " + e.getMessage());
                }

                try {
                    bookDao.save(nineteenEightyFour);
                    System.out.println("✅ 1984 saved with corrected year!");
                } catch (Exception e) {
                    System.out.println("❌ Error saving corrected 1984: " + e.getMessage());
                }

                // List all books
                System.out.println("\n📚 Books list (with categories):");
                List<Book> books = bookDao.findAll();
                books.forEach(book -> {
                    System.out.println("📖 " + book.getTitle() + " by " + book.getAuthor() +
                            " (" + book.getReleaseYear() + ") - Category: " + book.getCategory().getName());
                });

                // Test specific queries
                System.out.println("\n🔍 Testing specific queries...");

                // Find by author
                List<Book> martinBooks = bookDao.findByAuthor("Martin");
                System.out.println("Books by authors containing 'Martin': " + martinBooks.size());

                // Find by category
                List<Book> technicalBooks = bookDao.findByCategory(technical.getId());
                System.out.println("Technical books: " + technicalBooks.size());

                // Test category with most books
                Category mostBooks = categoryDao.findCategoryWithMostBooks();
                if (mostBooks != null) {
                    System.out.println("Category with most books: " + mostBooks.getName());
                } else {
                    System.out.println("No categories with books found");
                }

                // Test duplicate ISBN validation
                System.out.println("\n🧪 Testing duplicate ISBN validation:");
                try {
                    Book duplicate = new Book("Duplicate Book", "Different Author",
                            "Different synopsis", "9780132350884", 2020, technical); // Same ISBN as Clean Code
                    bookDao.save(duplicate);
                    System.out.println("❌ Duplicate ISBN validation failed!");
                } catch (Exception e) {
                    System.out.println("✅ Duplicate ISBN validation working: " + e.getMessage());
                }

                // Test category removal with books (should fail)
                System.out.println("\n🧪 Testing category removal with books:");
                try {
                    categoryDao.remove(technical.getId());
                    System.out.println("❌ Category removal validation failed!");
                } catch (Exception e) {
                    System.out.println("✅ Category removal validation working: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("❌ Error in operations: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\n📚 JDBC Version - System demonstration completed!");

        } catch (Exception e) {
            System.err.println("❌ Connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}