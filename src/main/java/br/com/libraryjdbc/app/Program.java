package br.com.libraryjdbc.app;

import java.sql.Connection;

import br.com.libraryjdbc.dao.BookDao;
import br.com.libraryjdbc.dao.CategoryDao;
import br.com.libraryjdbc.model.Category;
import db.DB;

public class Program {

    public static void main(String[] args) {
        try {
            Connection conn = DB.getConnection();
            System.out.println("✅ PostgreSQL connection established successfully!");
            
            // Teste simples de query
            boolean valid = conn.isValid(5);
            System.out.println("✅ Conexão é válida? " + valid);
            
            // TODO: Implement system menu
            System.out.println("🚀 Library Management System");
            
            CategoryDao categoryDao = new CategoryDao();
            BookDao bookDao = new BookDao();
            
            // Create tables
            categoryDao.createTable();
            bookDao.createTable();
            
            System.out.println("✅ Tables created successfully!");
            
            // Test category insertion
            System.out.println("\n📚 Testing category operations...");
            
            try {
                // Creating and saving some categories
                Category fiction = new Category("Fiction", "Fiction books including novels and short stories");
                Category technical = new Category("Technical", "Technical and programming books");
                Category scienceFiction = new Category("Science Fiction", "Science fiction and fantasy books");
                
                categoryDao.save(fiction);
                categoryDao.save(technical);
                categoryDao.save(scienceFiction);
                
                System.out.println("✅ Sample categories inserted!");
                
                // List all categories
                System.out.println("\n📋 Categories list:");
                categoryDao.findAll().forEach(System.out::println);
                
                // Test finding by ID
                System.out.println("\n🔍 Finding category by ID 1:");
                Category found = categoryDao.findById(1L);
                if (found != null) {
                    System.out.println("Found: " + found);
                } else {
                    System.out.println("Category not found");
                }
                
                // Test duplicate name (should show error)
                System.out.println("\n🧪 Testing duplicate name validation:");
                try {
                    categoryDao.save(new Category("Fiction", "Duplicate test"));
                } catch (Exception e) {
                    System.out.println("✅ Validation working: " + e.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error in category operations: " + e.getMessage());
            }
            
            System.out.println("\n📚 JDBC Version - Under development...");
            
        } catch (Exception e) {
            System.err.println("❌ Connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}