package br.com.libraryjdbc.dao;

import java.sql.Connection;
import java.sql.Statement;
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
            
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}