package br.com.libraryjdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.libraryjdbc.model.Book;
import br.com.libraryjdbc.model.Category;
import db.DB;
import db.DbException;


public class BookDao {

    public void createTable() {
        Connection conn = null;
        Statement st = null;

        try {
            conn = DB.getConnection();
            st = conn.createStatement();

            st.executeUpdate("CREATE TABLE IF NOT EXISTS book ("
                    + "id SERIAL PRIMARY KEY,"
                    + "title VARCHAR(200) NOT NULL,"
                    + "author VARCHAR(150) NOT NULL,"
                    + "synopsis TEXT,"
                    + "isbn VARCHAR(20) NOT NULL UNIQUE,"
                    + "release_year INTEGER NOT NULL CHECK (release_year >= 1967),"
                    + "category_id INTEGER NOT NULL,"
                    + "FOREIGN KEY (category_id) REFERENCES category(id)"
                    + ")");

            System.out.println("✅ Book table created or already exists!");

        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public Book save(Book book) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                throw new DbException("Book title cannot be empty");
            }

            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                throw new DbException("Book author cannot be empty");
            }

            if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
                throw new DbException("Book ISBN cannot be empty");
            }

            if (book.getReleaseYear() == null) {
                throw new DbException("Book release year cannot be null");
            }

            if (book.getReleaseYear() < 1967) {
                throw new DbException("Book release year must be >= 1967");
            }

            if (book.getCategory() == null || book.getCategory().getId() == null) {
                throw new DbException("Book must have a valid category");
            }

            if (isbnExists(book.getIsbn())) {
                throw new DbException("ISBN already exists: " + book.getIsbn());
            }

            if (!categoryExists(book.getCategory().getId())) {
                throw new DbException("Category with ID " + book.getCategory().getId() + " does not exist");
            }

            String sql = "INSERT INTO book (title, author, synopsis, isbn, release_year, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, book.getTitle());
            st.setString(2, book.getAuthor());
            st.setString(3, book.getSynopsis());
            st.setString(4, book.getIsbn());
            st.setInt(5, book.getReleaseYear());
            st.setLong(6, book.getCategory().getId());

            // Executing SQL
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    book.setId(id);
                    System.out.println("✅ Book inserted successfully! ID: " + id);
                }
            } else {
                throw new DbException("Unexpected error! No rows were affected.");
            }

            return book;

        } catch (SQLException e) {
            throw new DbException("Error inserting book: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public void update(Book book) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DB.getConnection();

            if (book.getId() == null) {
                throw new DbException("Book ID cannot be null for update");
            }

            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                throw new DbException("Book title cannot be empty");
            }

            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                throw new DbException("Book author cannot be empty");
            }

            if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
                throw new DbException("Book ISBN cannot be empty");
            }

            if (book.getReleaseYear() == null) {
                throw new DbException("Book release year cannot be null");
            }

            if (book.getReleaseYear() < 1967) {
                throw new DbException("Book release year must be >= 1967");
            }

            if (book.getCategory() == null || book.getCategory().getId() == null) {
                throw new DbException("Book must have a valid category");
            }

            if (!categoryExists(book.getCategory().getId())) {
                throw new DbException("Category with ID " + book.getCategory().getId() + " does not exist");
            }

            String sql = "UPDATE book SET title = ?, author = ?, synopsis = ?, isbn = ?, release_year = ?, category_id = ? WHERE id = ?";

            st = conn.prepareStatement(sql);

            st.setString(1, book.getTitle());
            st.setString(2, book.getAuthor());
            st.setString(3, book.getSynopsis());
            st.setString(4, book.getIsbn());
            st.setInt(5, book.getReleaseYear());
            st.setLong(6, book.getCategory().getId());
            st.setLong(7, book.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Book with ID " + book.getId() + " not found.");
            }

        } catch (SQLException e) {
            throw new DbException("Error updating book: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public void remove(Long id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DB.getConnection();

            String sql = "DELETE FROM book WHERE id = ?";

            st = conn.prepareStatement(sql);

            st.setLong(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Book with ID " + id + " not found.");
            }

        } catch (SQLException e) {
            throw new DbException("Error removing book: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public Book findById(Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            String sql = "SELECT b.*, c.name as category_name, c.description as category_description " +
                    "FROM book b " +
                    "INNER JOIN category c ON b.category_id = c.id " +
                    "WHERE b.id = ?";

            st = conn.prepareStatement(sql);

            st.setLong(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateBookWithCategory(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException("Error finding book by ID: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Book> findAll() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            String sql = "SELECT b.*, c.name as category_name, c.description as category_description " +
                    "FROM book b " +
                    "INNER JOIN category c ON b.category_id = c.id " +
                    "ORDER BY b.title";

            st = conn.prepareStatement(sql);

            rs = st.executeQuery();

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                books.add(instantiateBookWithCategory(rs));
            }

            return books;

        } catch (SQLException e) {
            throw new DbException("Error listing books: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Book> findByAuthor(String author) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            String sql = "SELECT b.*, c.name as category_name, c.description as category_description " +
                    "FROM book b " +
                    "INNER JOIN category c ON b.category_id = c.id " +
                    "WHERE LOWER(b.author) LIKE LOWER(?) " +
                    "ORDER BY b.title";

            st = conn.prepareStatement(sql);

            st.setString(1, "%" + author + "%");

            rs = st.executeQuery();

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                books.add(instantiateBookWithCategory(rs));
            }

            return books;

        } catch (SQLException e) {
            throw new DbException("Error finding books by author: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Book> findByCategory(Long categoryId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            String sql = "SELECT b.*, c.name as category_name, c.description as category_description " +
                    "FROM book b " +
                    "INNER JOIN category c ON b.category_id = c.id " +
                    "WHERE b.category_id = ? " +
                    "ORDER BY b.title";

            st = conn.prepareStatement(sql);

            // Setting parameter
            st.setLong(1, categoryId);

            // Executing SQL
            rs = st.executeQuery();

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                books.add(instantiateBookWithCategory(rs));
            }

            return books;

        } catch (SQLException e) {
            throw new DbException("Error finding books by category: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Helper methods

    private Book instantiateBookWithCategory(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setSynopsis(rs.getString("synopsis"));
        book.setIsbn(rs.getString("isbn"));
        book.setReleaseYear(rs.getInt("release_year"));

        Category category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setName(rs.getString("category_name"));
        category.setDescription(rs.getString("category_description"));

        book.setCategory(category);

        return book;
    }

    private boolean isbnExists(String isbn) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();
            String sql = "SELECT COUNT(*) FROM book WHERE isbn = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, isbn);

            rs = st.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new DbException("Error checking ISBN: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private boolean categoryExists(Long categoryId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();
            String sql = "SELECT COUNT(*) FROM category WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setLong(1, categoryId);

            rs = st.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new DbException("Error checking category existence: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}