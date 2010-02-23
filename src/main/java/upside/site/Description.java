package upside.site;

public class Description extends AbstractSiteElement {
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
        return maybeEquals(o) && deepEquals((SiteElement)o);
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : super.hashCode();
    }

    @Override
    public boolean deepEquals(SiteElement desc) {
        if (maybeEquals(desc)) {
            Description d = (Description)desc;
            return eq(getUrl(), d.getUrl()) && eq(getDescription(), d.getDescription());
        }
        return false;
    }
}
