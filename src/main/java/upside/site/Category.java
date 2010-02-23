package upside.site;

public class Category extends AbstractSiteElement {

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
        return maybeEquals(o) && ((Category)o).getName().equals(getName());
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

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Category c = (Category)se;
            return eq(getName(), c.getName())
                && eq(getLabel(), c.getLabel())
                && eq(getDescription(), c.getDescription());
        }
        return false;
    }

}
