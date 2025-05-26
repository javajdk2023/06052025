package br.com.libraryjdbc.model;

/**
 * Represents a book category in the library system.
 */
public class Category {

    private Long id;
    private String name;
    private String description;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     * Constructor with all attributes
     */
    public Category(Long id, String name, String description) {
        this.id = id;
        setName(name);
        setDescription(description);
    }

    /**
     * Constructor without ID (for new category insertion)
     */
    public Category(String name, String description) {
        setName(name);
        setDescription(description);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.trim().isEmpty()) {
            name = null; // Normalize empty strings to null
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.trim().isEmpty()) {
            description = null; // Normalize empty strings to null
        }
        this.description = description;
    }

    /**
     * Checks if category has valid name and description
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty()
                && description != null && !description.trim().isEmpty();
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
        Category other = (Category) obj;
        if (id == null) {
            return other.id == null;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Category{id=%d, name='%s', description='%s'%s}",
                id,
                name != null ? name : "null",
                description != null ? (description.length() > 50 ? description.substring(0, 47) + "..." : description) : "null",
                isValid() ? "" : " [INVALID]"
        );
    }
}