package br.com.libraryjdbc.test;

import br.com.libraryjdbc.dao.CategoryDao;
import br.com.libraryjdbc.model.Category;
import db.DB;
import db.DbException;

public class TestCategoryDao {

    public static void main(String[] args) {
        
        try {
            // Test connection
            DB.getConnection();
            System.out.println("✅ Test connection established successfully!");
            
            // Creating DAO instance
            CategoryDao categoryDao = new CategoryDao();
            
            // Ensure table exists
            categoryDao.createTable();
            
            // Testing category insertion
            testInsertCategory(categoryDao);
            
            // Testing category search
            testFindCategories(categoryDao);
            
            // Testing validations
            testValidations(categoryDao);
            
        } catch (DbException e) {
            System.err.println("❌ Error: " + e.getMessage());
        } finally {
            // Closing connection
            DB.closeConnection();
            System.out.println("✅ Test connection closed successfully!");
        }
    }
    
    private static void testInsertCategory(CategoryDao categoryDao) {
        System.out.println("\n=== CATEGORY INSERTION TEST ===");
        
        try {
            // Creating a new category
            Category horror = new Category("Horror", "Horror and thriller books");
            
            // Inserting category
            Category inserted = categoryDao.save(horror);
            
            if (inserted.getId() != null) {
                System.out.println("✅ Category inserted successfully: " + inserted);
            } else {
                System.out.println("❌ Insert failure: ID not returned");
            }
            
        } catch (DbException e) {
            System.err.println("❌ Error in insertion test: " + e.getMessage());
        }
    }
    
    private static void testFindCategories(CategoryDao categoryDao) {
        System.out.println("\n=== CATEGORY SEARCH TEST ===");
        
        try {
            // Listing all categories
            System.out.println("Categories list:");
            var categories = categoryDao.findAll();
            
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
            } else {
                categories.forEach(System.out::println);
                System.out.println("Total categories: " + categories.size());
            }
            
        } catch (DbException e) {
            System.err.println("❌ Error in search test: " + e.getMessage());
        }
    }
    
    private static void testValidations(CategoryDao categoryDao) {
        System.out.println("\n=== VALIDATION TESTS ===");
        
        // Test empty name
        try {
            categoryDao.save(new Category("", "Empty name test"));
            System.out.println("❌ Validation failed: Allowed empty name");
        } catch (DbException e) {
            System.out.println("✅ Empty name validation OK: " + e.getMessage());
        }
        
        // Test empty description
        try {
            categoryDao.save(new Category("Test Category", ""));
            System.out.println("❌ Validation failed: Allowed empty description");
        } catch (DbException e) {
            System.out.println("✅ Empty description validation OK: " + e.getMessage());
        }
        
        // Test duplicate name (assuming Horror was inserted before)
        try {
            categoryDao.save(new Category("Horror", "Another description"));
            System.out.println("❌ Validation failed: Allowed duplicate name");
        } catch (DbException e) {
            System.out.println("✅ Duplicate name validation OK: " + e.getMessage());
        }
    }
}