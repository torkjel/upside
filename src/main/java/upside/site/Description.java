package upside.site;

import upside.utils.Utils;

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
            return Utils.eq(d.getDescription(), getDescription())
                && Utils.eq(d.getUrl(), getUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : super.hashCode();
    }
}
