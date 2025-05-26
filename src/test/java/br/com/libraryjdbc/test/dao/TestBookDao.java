package br.com.libraryjdbc.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import br.com.libraryjdbc.dao.BookDao;
import br.com.libraryjdbc.dao.CategoryDao;
import br.com.libraryjdbc.model.Book;
import br.com.libraryjdbc.model.Category;
import db.DB;
import db.DbException;

/**
 * Comprehensive test class for BookDao CRUD operations
 * US-006: CRUD Completo de Livros - Validation Tests
 */
public class TestBookDao {

    private static BookDao bookDao;
    private static CategoryDao categoryDao;
    private static Category testCategory;

    public static void main(String[] args) {
        try {
            // Setup
            DB.getConnection();
            System.out.println("✅ Test connection established successfully!");

            bookDao = new BookDao();
            categoryDao = new CategoryDao();

            // Create tables
            categoryDao.createTable();
            bookDao.createTable();

            // Clean test data before starting
            cleanTestData();

            // Setup test category
            setupTestCategory();

            // Run all CRUD tests
            testSaveOperation();
            testFindByIdOperation();
            testFindAllOperation();
            testUpdateOperation();
            testRemoveOperation();
            testSpecificQueries();

            System.out.println("\n🎉 All BookDao CRUD tests completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error in BookDao tests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up test data
            cleanTestData();
            DB.closeConnection();
            System.out.println("✅ Test connection closed and cleanup completed!");
        }
    }

    // ==================== SAVE OPERATION TESTS ====================

    private static void testSaveOperation() {
        System.out.println("\n=== SAVE OPERATION TESTS ===");

        // Test 1: Valid book insertion
        testValidBookSave();

        // Test 2: Duplicate ISBN rejection
        testDuplicateIsbnSave();

        // Test 3: Invalid data rejection
        testInvalidDataSave();
    }

    private static void testValidBookSave() {
        System.out.println("\n--- Valid Book Save ---");

        try {
            Book book = new Book("Clean Code", "Robert C. Martin",
                    "A handbook of agile software craftsmanship",
                    "9780132350884", 2008, testCategory);

            Book saved = bookDao.save(book);

            if (saved.getId() != null && saved.getId() > 0) {
                System.out.println("✅ Valid book saved successfully with ID: " + saved.getId());
            } else {
                System.out.println("❌ Failed to save valid book - ID not generated");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error saving valid book: " + e.getMessage());
        }
    }

    private static void testDuplicateIsbnSave() {
        System.out.println("\n--- Duplicate ISBN Save Test ---");

        try {
            // Try to save book with same ISBN
            Book duplicate = new Book("Different Title", "Different Author",
                    "Different synopsis", "9780132350884", 2010, testCategory);
            bookDao.save(duplicate);

            System.out.println("❌ VALIDATION FAILURE: Duplicate ISBN was allowed!");

        } catch (DbException e) {
            if (e.getMessage().contains("ISBN already exists")) {
                System.out.println("✅ Duplicate ISBN validation working: " + e.getMessage());
            } else {
                System.out.println("❌ Unexpected error message: " + e.getMessage());
            }
        }
    }

    private static void testInvalidDataSave() {
        System.out.println("\n--- Invalid Data Save Tests ---");

        // Test empty title
        try {
            Book emptyTitle = new Book("", "Valid Author", "Synopsis", "1234567890", 2000, testCategory);
            bookDao.save(emptyTitle);
            System.out.println("❌ VALIDATION FAILURE: Empty title was allowed!");
        } catch (DbException e) {
            if (e.getMessage().contains("title cannot be empty")) {
                System.out.println("✅ Empty title validation working");
            } else {
                System.out.println("❌ Unexpected error for empty title: " + e.getMessage());
            }
        }

        // Test invalid year (< 1967)
        try {
            Book invalidYear = new Book("Old Book", "Old Author", "Synopsis", "1111111111", 1950, testCategory);
            bookDao.save(invalidYear);
            System.out.println("❌ VALIDATION FAILURE: Invalid year was allowed!");
        } catch (DbException e) {
            if (e.getMessage().contains("must be >= 1967")) {
                System.out.println("✅ Invalid year validation working");
            } else {
                System.out.println("❌ Unexpected error for invalid year: " + e.getMessage());
            }
        }

        // Test null category
        try {
            Book nullCategory = new Book("Title", "Author", "Synopsis", "2222222222", 2000, null);
            bookDao.save(nullCategory);
            System.out.println("❌ VALIDATION FAILURE: Null category was allowed!");
        } catch (DbException e) {
            if (e.getMessage().contains("valid category")) {
                System.out.println("✅ Null category validation working");
            } else {
                System.out.println("❌ Unexpected error for null category: " + e.getMessage());
            }
        }

        // Test invalid category ID
        try {
            Category invalidCategory = new Category();
            invalidCategory.setId(999999L);
            Book invalidCategoryBook = new Book("Title", "Author", "Synopsis", "3333333333", 2000, invalidCategory);
            bookDao.save(invalidCategoryBook);
            System.out.println("❌ VALIDATION FAILURE: Invalid category ID was allowed!");
        } catch (DbException e) {
            if (e.getMessage().contains("does not exist")) {
                System.out.println("✅ Invalid category ID validation working");
            } else {
                System.out.println("❌ Unexpected error for invalid category: " + e.getMessage());
            }
        }
    }

    // ==================== FIND BY ID OPERATION TESTS ====================

    private static void testFindByIdOperation() {
        System.out.println("\n=== FIND BY ID OPERATION TESTS ===");

        // Test 1: Find existing book
        testFindExistingBook();

        // Test 2: Find non-existing book
        testFindNonExistingBook();

        // Test 3: Verify category data is included
        testBookWithCategoryData();
    }

    private static void testFindExistingBook() {
        System.out.println("\n--- Find Existing Book ---");

        try {
            // First ensure we have a book
            Book effective = new Book("Effective Java", "Joshua Bloch",
                    "Best practices for Java programming", "9780134685991", 2017, testCategory);
            Book saved = bookDao.save(effective);

            // Now find it
            Book found = bookDao.findById(saved.getId());

            if (found != null && found.getId().equals(saved.getId())) {
                System.out.println("✅ Existing book found successfully: " + found.getTitle());
            } else {
                System.out.println("❌ Failed to find existing book");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error finding existing book: " + e.getMessage());
        }
    }

    private static void testFindNonExistingBook() {
        System.out.println("\n--- Find Non-Existing Book ---");

        try {
            Book notFound = bookDao.findById(999999L);

            if (notFound == null) {
                System.out.println("✅ Non-existing book correctly returned null");
            } else {
                System.out.println("❌ Non-existing book should return null but returned: " + notFound);
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error for non-existing book: " + e.getMessage());
        }
    }

    private static void testBookWithCategoryData() {
        System.out.println("\n--- Book With Category Data Test ---");

        try {
            // Create and save a book
            Book design = new Book("Design Patterns", "Gang of Four",
                    "Elements of reusable object-oriented software", "9780201633610", 1994, testCategory);
            Book saved = bookDao.save(design);

            // Find it and verify category data
            Book found = bookDao.findById(saved.getId());

            if (found != null && found.getCategory() != null &&
                    found.getCategory().getName() != null &&
                    found.getCategory().getDescription() != null) {
                System.out.println("✅ Book found with complete category data: " + found.getCategory().getName());
            } else {
                System.out.println("❌ Book found but category data is incomplete");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error testing category data: " + e.getMessage());
        }
    }

    // ==================== FIND ALL OPERATION TESTS ====================

    private static void testFindAllOperation() {
        System.out.println("\n=== FIND ALL OPERATION TESTS ===");

        // Test 1: List multiple books
        testListMultipleBooks();

        // Test 2: Verify ordering by title
        testBookOrdering();

        // Test 3: Verify all books have category data
        testAllBooksWithCategories();
    }

    private static void testListMultipleBooks() {
        System.out.println("\n--- List Multiple Books ---");

        try {
            // Ensure we have multiple books
            bookDao.save(new Book("Java Concurrency", "Brian Goetz",
                    "Java concurrency in practice", "9780321349606", 2006, testCategory));
            bookDao.save(new Book("Spring in Action", "Craig Walls",
                    "Spring framework guide", "9781617294945", 2018, testCategory));

            List<Book> books = bookDao.findAll();

            if (books != null && books.size() >= 2) {
                System.out.println("✅ Multiple books listed successfully. Count: " + books.size());
                System.out.println("Books: " + books.stream()
                        .map(Book::getTitle)
                        .limit(3)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None"));
            } else {
                System.out.println("❌ Failed to list multiple books");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error listing books: " + e.getMessage());
        }
    }

    private static void testBookOrdering() {
        System.out.println("\n--- Book Ordering Test ---");

        try {
            // Insert books in non-alphabetical order
            bookDao.save(new Book("Zebra Programming", "Z Author", "About zebras", "9999999999", 2020, testCategory));
            bookDao.save(new Book("Apple Development", "A Author", "About apples", "8888888888", 2019, testCategory));

            List<Book> orderedList = bookDao.findAll();

            if (orderedList.size() >= 2) {
                boolean isOrdered = true;
                for (int i = 1; i < orderedList.size(); i++) {
                    if (orderedList.get(i-1).getTitle().compareTo(orderedList.get(i).getTitle()) > 0) {
                        isOrdered = false;
                        break;
                    }
                }

                if (isOrdered) {
                    System.out.println("✅ Books correctly ordered alphabetically by title");
                } else {
                    System.out.println("❌ Books not properly ordered");
                }
            } else {
                System.out.println("❌ Not enough books to test ordering");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error testing ordering: " + e.getMessage());
        }
    }

    private static void testAllBooksWithCategories() {
        System.out.println("\n--- All Books With Categories Test ---");

        try {
            List<Book> books = bookDao.findAll();

            boolean allHaveCategories = books.stream()
                    .allMatch(book -> book.getCategory() != null &&
                            book.getCategory().getName() != null);

            if (allHaveCategories) {
                System.out.println("✅ All books include complete category data");
            } else {
                System.out.println("❌ Some books missing category data");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error checking categories: " + e.getMessage());
        }
    }

    // ==================== UPDATE OPERATION TESTS ====================

    private static void testUpdateOperation() {
        System.out.println("\n=== UPDATE OPERATION TESTS ===");

        // Test 1: Valid update
        testValidUpdate();

        // Test 2: Update non-existing book
        testUpdateNonExisting();

        // Test 3: Update with invalid data
        testUpdateWithInvalidData();
    }

    private static void testValidUpdate() {
        System.out.println("\n--- Valid Update Test ---");

        try {
            // Create book to update
            Book original = bookDao.save(new Book("Original Title", "Original Author",
                    "Original synopsis", "5555555555", 2000, testCategory));

            // Update it
            original.setTitle("Updated Title");
            original.setAuthor("Updated Author");
            original.setSynopsis("Updated synopsis");
            bookDao.update(original);

            // Verify update
            Book updated = bookDao.findById(original.getId());

            if (updated != null &&
                    "Updated Title".equals(updated.getTitle()) &&
                    "Updated Author".equals(updated.getAuthor())) {
                System.out.println("✅ Book updated successfully");
            } else {
                System.out.println("❌ Book update failed or data not persisted");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error in valid update: " + e.getMessage());
        }
    }

    private static void testUpdateNonExisting() {
        System.out.println("\n--- Update Non-Existing Book ---");

        try {
            Book nonExisting = new Book("Non Existing", "Author", "Synopsis", "6666666666", 2000, testCategory);
            nonExisting.setId(999999L);

            bookDao.update(nonExisting);
            System.out.println("❌ VALIDATION FAILURE: Update of non-existing book was allowed!");

        } catch (DbException e) {
            if (e.getMessage().contains("not found")) {
                System.out.println("✅ Update non-existing book validation working: " + e.getMessage());
            } else {
                System.out.println("❌ Unexpected error message: " + e.getMessage());
            }
        }
    }

    private static void testUpdateWithInvalidData() {
        System.out.println("\n--- Update With Invalid Data ---");

        try {
            // Create valid book first
            Book valid = bookDao.save(new Book("Valid for Update", "Valid Author",
                    "Valid synopsis", "7777777777", 2000, testCategory));

            // Test null ID
            try {
                Book nullId = new Book("Title", "Author", "Synopsis", "8888888888", 2000, testCategory);
                nullId.setId(null);
                bookDao.update(nullId);
                System.out.println("❌ VALIDATION FAILURE: Update with null ID was allowed!");
            } catch (DbException e) {
                if (e.getMessage().contains("ID cannot be null")) {
                    System.out.println("✅ Null ID validation working");
                } else {
                    System.out.println("❌ Unexpected error for null ID: " + e.getMessage());
                }
            }

            // Test invalid year
            try {
                valid.setReleaseYear(1950);
                bookDao.update(valid);
                System.out.println("❌ VALIDATION FAILURE: Update with invalid year was allowed!");
            } catch (DbException e) {
                if (e.getMessage().contains("must be >= 1967")) {
                    System.out.println("✅ Invalid year update validation working");
                } else {
                    System.out.println("❌ Unexpected error for invalid year: " + e.getMessage());
                }
            }

        } catch (DbException e) {
            System.out.println("❌ Setup error for invalid data test: " + e.getMessage());
        }
    }

    // ==================== REMOVE OPERATION TESTS ====================

    private static void testRemoveOperation() {
        System.out.println("\n=== REMOVE OPERATION TESTS ===");

        // Test 1: Valid removal
        testValidRemoval();

        // Test 2: Remove non-existing book
        testRemoveNonExisting();
    }

    private static void testValidRemoval() {
        System.out.println("\n--- Valid Removal Test ---");

        try {
            // Create book to remove
            Book toRemove = bookDao.save(new Book("ToRemove" + System.currentTimeMillis(),
                    "Remove Author", "To be removed", "9999999998", 2000, testCategory));
            Long idToRemove = toRemove.getId();

            // Remove it
            bookDao.remove(idToRemove);

            // Verify removal
            Book removed = bookDao.findById(idToRemove);

            if (removed == null) {
                System.out.println("✅ Book removed successfully");
            } else {
                System.out.println("❌ Book removal failed - still exists");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error in removal: " + e.getMessage());
        }
    }

    private static void testRemoveNonExisting() {
        System.out.println("\n--- Remove Non-Existing Book ---");

        try {
            bookDao.remove(999999L);
            System.out.println("❌ VALIDATION FAILURE: Removal of non-existing book was allowed!");

        } catch (DbException e) {
            if (e.getMessage().contains("not found")) {
                System.out.println("✅ Remove non-existing book validation working: " + e.getMessage());
            } else {
                System.out.println("❌ Unexpected error message: " + e.getMessage());
            }
        }
    }

    // ==================== SPECIFIC QUERIES TESTS ====================

    private static void testSpecificQueries() {
        System.out.println("\n=== SPECIFIC QUERIES TESTS ===");

        // Test 1: Find by author
        testFindByAuthor();

        // Test 2: Find by category
        testFindByCategory();
    }

    private static void testFindByAuthor() {
        System.out.println("\n--- Find By Author Test ---");

        try {
            // Create books by specific author
            bookDao.save(new Book("Book One", "Martin Fowler", "First book", "1111111111", 2000, testCategory));
            bookDao.save(new Book("Book Two", "Martin Fowler", "Second book", "2222222222", 2001, testCategory));
            bookDao.save(new Book("Other Book", "Other Author", "Different author", "3333333333", 2002, testCategory));

            // Search for Martin Fowler books
            List<Book> martinBooks = bookDao.findByAuthor("Martin Fowler");

            if (martinBooks.size() == 2) {
                System.out.println("✅ Find by author working correctly - found " + martinBooks.size() + " books");
            } else {
                System.out.println("❌ Find by author failed - expected 2, found " + martinBooks.size());
            }

            // Test case-insensitive search
            List<Book> caseInsensitive = bookDao.findByAuthor("martin fowler");

            if (caseInsensitive.size() == 2) {
                System.out.println("✅ Case-insensitive search working");
            } else {
                System.out.println("❌ Case-insensitive search failed");
            }

            // Test partial match
            List<Book> partialMatch = bookDao.findByAuthor("Martin");

            if (partialMatch.size() == 2) {
                System.out.println("✅ Partial match search working");
            } else {
                System.out.println("❌ Partial match search failed");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error in find by author: " + e.getMessage());
        }
    }

    private static void testFindByCategory() {
        System.out.println("\n--- Find By Category Test ---");

        try {
            // Create another category for testing
            Category anotherCategory = categoryDao.save(new Category("Science", "Science books"));

            // Create books in different categories
            bookDao.save(new Book("Tech Book 1", "Tech Author", "About tech", "4444444444", 2000, testCategory));
            bookDao.save(new Book("Tech Book 2", "Tech Author", "More tech", "5555555555", 2001, testCategory));
            bookDao.save(new Book("Science Book", "Science Author", "About science", "6666666666", 2002, anotherCategory));

            // Search for books in test category
            List<Book> techBooks = bookDao.findByCategory(testCategory.getId());

            if (techBooks.size() >= 2) {
                System.out.println("✅ Find by category working - found " + techBooks.size() + " books in category");
            } else {
                System.out.println("❌ Find by category failed - expected at least 2, found " + techBooks.size());
            }

            // Verify books include category data
            boolean allHaveCategoryData = techBooks.stream()
                    .allMatch(book -> book.getCategory() != null &&
                            book.getCategory().getName() != null);

            if (allHaveCategoryData) {
                System.out.println("✅ All books in category search include category data");
            } else {
                System.out.println("❌ Some books missing category data in category search");
            }

        } catch (DbException e) {
            System.out.println("❌ Unexpected error in find by category: " + e.getMessage());
        }
    }

    // ==================== HELPER METHODS ====================

    private static void setupTestCategory() {
        try {
            // Create a test category for all book tests
            testCategory = categoryDao.save(new Category("Technical" + System.currentTimeMillis(),
                    "Technical books for testing"));
            System.out.println("✅ Test category created: " + testCategory.getName());

        } catch (DbException e) {
            // Try to find existing category
            List<Category> categories = categoryDao.findAll();
            if (!categories.isEmpty()) {
                testCategory = categories.get(0);
                System.out.println("✅ Using existing category: " + testCategory.getName());
            } else {
                throw new DbException("Cannot create or find test category: " + e.getMessage());
            }
        }
    }

    private static void cleanTestData() {
        try {
            Connection conn = DB.getConnection();

            // Clean books first (FK dependency)
            try (PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM book WHERE isbn LIKE '%TEST%' OR title LIKE '%Test%' OR " +
                            "isbn IN ('9780132350884', '9780134685991', '9780201633610', '1234567890', " +
                            "'1111111111', '2222222222', '3333333333', '4444444444', '5555555555', " +
                            "'6666666666', '7777777777', '8888888888', '9999999999', '9999999998') OR " +
                            "title LIKE '%Clean Code%' OR title LIKE '%Effective Java%' OR title LIKE '%Design Patterns%' OR " +
                            "title LIKE '%Java Concurrency%' OR title LIKE '%Spring in Action%' OR title LIKE '%Zebra%' OR " +
                            "title LIKE '%Apple%' OR title LIKE '%Original%' OR title LIKE '%Updated%' OR " +
                            "title LIKE '%Valid for Update%' OR title LIKE '%ToRemove%' OR title LIKE '%Book One%' OR " +
                            "title LIKE '%Book Two%' OR title LIKE '%Other Book%' OR title LIKE '%Tech Book%' OR " +
                            "title LIKE '%Science Book%'")) {
                st.executeUpdate();
            }

            // Clean test categories
            try (PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM category WHERE name LIKE '%Test%' OR name LIKE '%Technical%' OR " +
                            "name LIKE '%Science%' AND description LIKE '%test%'")) {
                st.executeUpdate();
            }

        } catch (SQLException e) {
            // Ignore cleanup errors - they're not critical for tests
        }
    }
}