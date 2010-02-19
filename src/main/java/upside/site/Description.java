package upside.site;

public class Description {
    private String url;
    private String description;

    public Description(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        else if (o instanceof Description) {
            Description d = (Description)o;
            return eq(d.getDescription(), getDescription())
                && eq(d.getUrl(), getUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : super.hashCode();
    }

    private boolean eq(Object o1, Object o2) {
        return (o1 != null && o1.equals(o2)) || (o1 == null && o2 == null);
    }
}
