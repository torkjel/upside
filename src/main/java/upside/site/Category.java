package upside.site;

public class Category {
    private String name;
    private String label;
    private String description;

    public Category(String name, String label, String description) {
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return (o instanceof Category)
            && ((Category)o).getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ":[" +
            "name:" + name + ", " +
            "label:" + label + ", " +
            "description:" + description + "]";
    }
}
