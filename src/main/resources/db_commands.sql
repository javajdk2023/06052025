-- ========================================
-- DATABASE STRUCTURE
-- ========================================

-- Category Table
CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL
);

-- Book Table
CREATE TABLE IF NOT EXISTS book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(150) NOT NULL,
    synopsis TEXT,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    release_year INTEGER NOT NULL CHECK (release_year >= 1967),
    category_id INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- ========================================
-- INSERT EXAMPLES
-- ========================================

-- Categories
INSERT INTO category (name, description) 
VALUES ('Fiction', 'Fiction books including novels and short stories');

INSERT INTO category (name, description) 
VALUES ('Technical', 'Technical and academic books');

-- Books
INSERT INTO book (title, author, synopsis, isbn, release_year, category_id)
VALUES (
    'Clean Code',
    'Robert C. Martin',
    'A handbook of agile software craftsmanship',
    '9780132350884',
    2008,
    (SELECT id FROM category WHERE name = 'Technical')
);

-- ========================================
-- SELECT EXAMPLES
-- ========================================

-- Basic Selects
SELECT * FROM category;
SELECT * FROM book;

-- Find book by ISBN
SELECT * FROM book WHERE isbn = '9780132350884';

-- Find books by author (case insensitive)
SELECT * FROM book WHERE LOWER(author) LIKE LOWER('%martin%');

-- Books with category information
SELECT b.*, c.name as category_name 
FROM book b 
JOIN category c ON b.category_id = c.id;

-- Books from specific category
SELECT b.* 
FROM book b 
JOIN category c ON b.category_id = c.id 
WHERE c.name = 'Technical';

-- ========================================
-- UPDATE EXAMPLES
-- ========================================

-- Update category description
UPDATE category 
SET description = 'Technical books about programming and software development' 
WHERE name = 'Technical';

-- Update book synopsis
UPDATE book 
SET synopsis = 'Updated synopsis text' 
WHERE isbn = '9780132350884';

-- ========================================
-- DELETE EXAMPLES
-- ========================================

-- Delete book
DELETE FROM book WHERE isbn = '9780132350884';

-- Delete category (will fail if has books)
DELETE FROM category WHERE name = 'Fiction';

-- ========================================
-- USEFUL QUERIES
-- ========================================

-- Count books per category
SELECT c.name, COUNT(b.id) as book_count
FROM category c
LEFT JOIN book b ON c.id = c.id
GROUP BY c.name
ORDER BY book_count DESC;

-- Find category with most books
SELECT c.name, COUNT(b.id) as book_count
FROM category c
LEFT JOIN book b ON c.id = c.id
GROUP BY c.name
ORDER BY book_count DESC
LIMIT 1;

-- Books released after year 2000
SELECT * FROM book WHERE release_year >= 2000;