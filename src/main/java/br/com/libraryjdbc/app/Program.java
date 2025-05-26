package br.com.libraryjdbc.app;

import java.sql.Connection;

import br.com.libraryjdbc.dao.BookDao;
import br.com.libraryjdbc.dao.CategoryDao;
import br.com.libraryjdbc.model.Book;
import br.com.libraryjdbc.model.Category;
import db.DB;

public class Program {

    public static void main(String[] args) {
        try {
            Connection conn = DB.getConnection();
            System.out.println("✅ Database connected successfully!");

            CategoryDao categoryDao = new CategoryDao();
            BookDao bookDao = new BookDao();

            categoryDao.createTable();
            bookDao.createTable();

            Category technical = new Category("physycal", "Programming physycal books");
            Category savedCategory = categoryDao.save(technical);
            System.out.println("📂 Category created: " + savedCategory);

            Book cleanCode = new Book("Clean Code", "Robert C. Martin",
                    "A handbook of agile software craftsmanship",
                    "9788132350874", 2008, savedCategory);
            Book savedBook = bookDao.save(cleanCode);
            System.out.println("📖 Book created: " + savedBook);

            Category foundCategory = categoryDao.findById(savedCategory.getId());
            Book foundBook = bookDao.findById(savedBook.getId());

            System.out.println("\n✅ Verification:");
            System.out.println("Category valid: " + foundCategory.isValid());
            System.out.println("Book valid: " + foundBook.isValid());
            System.out.println("Book category: " + foundBook.getCategoryName());

            System.out.println("\n🎉 Library system working correctly!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        } finally {
            DB.closeConnection();
        }
    }
}