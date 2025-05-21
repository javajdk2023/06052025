package br.com.libraryjdbc.app;

import java.sql.Connection;

import br.com.libraryjdbc.dao.BookDao;
import br.com.libraryjdbc.dao.CategoryDao;
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
            
            
            System.out.println("📚 JDBC Version - Under development...");
            
        } catch (Exception e) {
            System.err.println("❌ Connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}
