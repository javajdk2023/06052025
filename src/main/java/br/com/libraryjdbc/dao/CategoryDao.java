package br.com.libraryjdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.libraryjdbc.model.Category;
import db.DB;
import db.DbException;

public class CategoryDao {
    
    public void createTable() {
        Connection conn = null;
        Statement st = null;
        
        try {
            conn = DB.getConnection();
            st = conn.createStatement();
            
            st.executeUpdate("CREATE TABLE IF NOT EXISTS category ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL UNIQUE,"
                    + "description TEXT NOT NULL"
                    + ")");
            
            System.out.println("Category table created or already exists!");
            
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public Category save(Category category) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new DbException("Category name cannot be empty");
            }
            
            if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
                throw new DbException("Category description cannot be empty");
            }
            
            if (categoryNameExists(category.getName())) {
                throw new DbException("Category name already exists: " + category.getName());
            }
            
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
            
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, category.getName());
            st.setString(2, category.getDescription());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    category.setId(id);
                    System.out.println("✅ Category inserted successfully! ID: " + id);
                }
            } else {
                throw new DbException("Unexpected error! No rows were affected.");
            }
            
            return category;
            
        } catch (SQLException e) {
            throw new DbException("Error inserting category: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public void update(Category category) {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DB.getConnection();
            
            if (category.getId() == null) {
                throw new DbException("Category ID cannot be null for update");
            }
            
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new DbException("Category name cannot be empty");
            }
            
            if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
                throw new DbException("Category description cannot be empty");
            }
            
            String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
            
            st = conn.prepareStatement(sql);
            
            st.setString(1, category.getName());
            st.setString(2, category.getDescription());
            st.setLong(3, category.getId());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new DbException("Category with ID " + category.getId() + " not found.");
            }
            
        } catch (SQLException e) {
            throw new DbException("Error updating category: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public void remove(Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DB.getConnection();
            
            if (categoryHasBooks(id)) {
                throw new DbException("Cannot remove category that has associated books");
            }
            
            String sql = "DELETE FROM category WHERE id = ?";
            
            st = conn.prepareStatement(sql);
            
            st.setLong(1, id);
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new DbException("Category with ID " + id + " not found.");
            }
            
        } catch (SQLException e) {
            throw new DbException("Error removing category: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public Category findById(Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT * FROM category WHERE id = ?";
            
            st = conn.prepareStatement(sql);
            
            st.setLong(1, id);
            
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateCategory(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DbException("Error finding category by ID: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Category> findAll() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT * FROM category ORDER BY name";
            
            st = conn.prepareStatement(sql);
            
            rs = st.executeQuery();
            
            List<Category> categories = new ArrayList<>();
            
            while (rs.next()) {
                categories.add(instantiateCategory(rs));
            }
            
            return categories;
            
        } catch (SQLException e) {
            throw new DbException("Error listing categories: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public Category findCategoryWithMostBooks() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT c.*, COUNT(b.id) as book_count " +
                        "FROM category c " +
                        "LEFT JOIN book b ON c.id = b.category_id " +
                        "GROUP BY c.id, c.name, c.description " +
                        "ORDER BY book_count DESC " +
                        "LIMIT 1";
            
            st = conn.prepareStatement(sql);
            
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateCategory(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DbException("Error finding category with most books: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    // Helper methods
    
    private Category instantiateCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        return category;
    }
    
    private boolean categoryNameExists(String name) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            String sql = "SELECT COUNT(*) FROM category WHERE LOWER(name) = LOWER(?)";
            st = conn.prepareStatement(sql);
            st.setString(1, name);
            
            rs = st.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            throw new DbException("Error checking category name: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    private boolean categoryHasBooks(Long categoryId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DB.getConnection();
            String sql = "SELECT COUNT(*) FROM book WHERE category_id = ?";
            st = conn.prepareStatement(sql);
            st.setLong(1, categoryId);
            
            rs = st.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            throw new DbException("Error checking category books: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}