package br.com.libraryjdbc.model;

/**
 * Represents a book in the library system.
 */
public class Book {

    private Long id;
    private String title;
    private String author;
    private String synopsis;
    private String isbn;
    private Integer releaseYear;
    private Category category;

    /**
     * Default constructor
     */
    public Book() {
    }

    /**
     * Constructor with all attributes
     */
    public Book(Long id, String title, String author, String synopsis, String isbn, Integer releaseYear, Category category) {
        this.id = id;
        setTitle(title);
        setAuthor(author);
        setSynopsis(synopsis);
        setIsbn(isbn);
        setReleaseYear(releaseYear);
        setCategory(category);
    }

    /**
     * Constructor without ID (for new book insertion)
     */
    public Book(String title, String author, String synopsis, String isbn, Integer releaseYear, Category category) {
        setTitle(title);
        setAuthor(author);
        setSynopsis(synopsis);
        setIsbn(isbn);
        setReleaseYear(releaseYear);
        setCategory(category);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && title.trim().isEmpty()) {
            title = null; // Normalize empty strings to null
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author != null && author.trim().isEmpty()) {
            author = null; // Normalize empty strings to null
        }
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        if (synopsis != null && synopsis.trim().isEmpty()) {
            synopsis = null; // Normalize empty strings to null
        }
        this.synopsis = synopsis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn != null && isbn.trim().isEmpty()) {
            isbn = null; // Normalize empty strings to null
        }
        this.isbn = isbn;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Checks if book has all required fields
     */
    public boolean isValid() {
        return title != null && !title.trim().isEmpty()
                && author != null && !author.trim().isEmpty()
                && isbn != null && !isbn.trim().isEmpty()
                && releaseYear != null
                && category != null;
    }

    /**
     * Gets category name safely
     */
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', isbn='%s', year=%d, category='%s'%s}",
                id,
                title != null ? title : "null",
                author != null ? author : "null",
                isbn != null ? isbn : "null",
                releaseYear != null ? releaseYear : 0,
                getCategoryName() != null ? getCategoryName() : "null",
                isValid() ? "" : " [INVALID]"
        );
    }
}