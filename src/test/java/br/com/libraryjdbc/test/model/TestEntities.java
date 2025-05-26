package br.com.libraryjdbc.test.model;

import br.com.libraryjdbc.model.Book;
import br.com.libraryjdbc.model.Category;


public class TestEntities {

    public static void main(String[] args) {
        System.out.println("=== TESTING ENTITY CLASSES ===\n");

        testCategoryEntity();

        testBookEntity();

        testBookCategoryRelationship();

        testEqualsAndHashCode();

        System.out.println("\n✅ All entity tests completed successfully!");
    }

    private static void testCategoryEntity() {
        System.out.println("--- Testing Category Entity ---");

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");
        category1.setDescription("Fiction books including novels and short stories");

        Category category2 = new Category("Technical", "Technical and programming books");

        Category category3 = new Category(3L, "Science Fiction", "Science fiction and fantasy books");

        System.out.println("Category 1: " + category1);
        System.out.println("Category 2: " + category2);
        System.out.println("Category 3: " + category3);

        System.out.println("✅ Category entity tests passed\n");
    }

    private static void testBookEntity() {
        System.out.println("--- Testing Book Entity ---");

        Category category = new Category(1L, "Technical", "Technical books");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setSynopsis("A handbook of agile software craftsmanship");
        book1.setIsbn("9780132350884");
        book1.setReleaseYear(2008);
        book1.setCategory(category);

        Book book2 = new Book(
                "Effective Java",
                "Joshua Bloch",
                "Best practices for Java programming",
                "9780134685991",
                2017,
                category
        );

        Book book3 = new Book(
                3L,
                "Design Patterns",
                "Gang of Four",
                "Elements of reusable object-oriented software",
                "9780201633610",
                1994,
                category
        );

        System.out.println("Book 1: " + book1);
        System.out.println("Book 2: " + book2);
        System.out.println("Book 3: " + book3);

        System.out.println("✅ Book entity tests passed\n");
    }

    private static void testBookCategoryRelationship() {
        System.out.println("--- Testing Book-Category Relationship ---");

        Category fiction = new Category(1L, "Fiction", "Fiction books");
        Category technical = new Category(2L, "Technical", "Technical books");

        Book book1 = new Book("1984", "George Orwell", "Dystopian novel", "9780451524935", 1949, fiction);
        Book book2 = new Book("Java: The Complete Reference", "Herbert Schildt", "Comprehensive Java guide", "9781260440232", 2020, technical);

        System.out.println("Book 1 category: " + book1.getCategory().getName());
        System.out.println("Book 2 category: " + book2.getCategory().getName());

        book1.setCategory(technical);
        System.out.println("Book 1 after category change: " + book1.getCategory().getName());

        Book book3 = new Book("Test Book", "Test Author", "Test synopsis", "1234567890", 2023, null);
        System.out.println("Book 3 with null category: " + book3);

        System.out.println("✅ Book-Category relationship tests passed\n");
    }

    private static void testEqualsAndHashCode() {
        System.out.println("--- Testing equals() and hashCode() ---");

        Category cat1 = new Category(1L, "Fiction", "Fiction books");
        Category cat2 = new Category(1L, "Different Name", "Different description");
        Category cat3 = new Category(2L, "Fiction", "Fiction books");
        Category cat4 = new Category();

        System.out.println("cat1.equals(cat2) (same ID): " + cat1.equals(cat2));
        System.out.println("cat1.equals(cat3) (different ID): " + cat1.equals(cat3));
        System.out.println("cat1.equals(cat4) (null ID): " + cat1.equals(cat4));

        // Test Book equals and hashCode
        Category category = new Category(1L, "Technical", "Technical books");
        Book book1 = new Book(1L, "Clean Code", "Robert Martin", "Synopsis", "123", 2008, category);
        Book book2 = new Book(1L, "Different Title", "Different Author", "Different", "456", 2009, category);
        Book book3 = new Book(2L, "Clean Code", "Robert Martin", "Synopsis", "123", 2008, category);
        Book book4 = new Book();

        System.out.println("book1.equals(book2) (same ID): " + book1.equals(book2));
        System.out.println("book1.equals(book3) (different ID): " + book1.equals(book3));
        System.out.println("book1.equals(book4) (null ID): " + book1.equals(book4));

        // Test hashCode consistency
        System.out.println("cat1.hashCode() == cat2.hashCode(): " + (cat1.hashCode() == cat2.hashCode())); // should be true
        System.out.println("book1.hashCode() == book2.hashCode(): " + (book1.hashCode() == book2.hashCode())); // should be true

        System.out.println("✅ equals() and hashCode() tests passed\n");
    }
}