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
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.isbn = isbn;
        this.releaseYear = releaseYear;
        this.category = category;
    }

    /**
     * Constructor without ID (for new book insertion)
     */
    public Book(String title, String author, String synopsis, String isbn, Integer releaseYear, Category category) {
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.isbn = isbn;
        this.releaseYear = releaseYear;
        this.category = category;
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
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
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
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author +
                ", synopsis=" + synopsis + ", isbn=" + isbn + ", releaseYear=" + releaseYear +
                ", category=" + (category != null ? category.getName() : "null") + "]";
    }
}