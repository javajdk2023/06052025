package br.com.libraryjdbc.dao;

import java.sql.Connection;
import java.sql.Statement;
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
            
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}
