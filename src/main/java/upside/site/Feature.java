package upside.site;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import upside.utils.Utils;

public class Feature {
    private String url;
    private String id;
    private String version;
    private boolean patch = false;
    private Set<Category> categories = new HashSet<Category>();

    public Feature(String url, String id, String version, boolean patch, Category category) {
        this(url, id, version, patch, Collections.singletonList(category));
    }

    public Feature(String url, String id, String version, boolean patch, Collection<Category> categories) {
        this.url = url;
        this.id = id;
        this.version = version;
        this.patch = patch;
        this.categories = new HashSet<Category>(categories);
    }

    public Feature withAbsoluteUrl(URL origin) {
        return new Feature(Utils.absoluteUrl(origin, url), id, version, patch, categories);
    }

    public Feature withCategory(Category cat) {
        Set<Category> categories = new HashSet<Category>(this.categories);
        categories.add(cat);
        return new Feature(url, id, version, patch, categories);
    }

    public Feature moveToCategory(Category cat) {
        return new Feature(url, id, version, patch, cat);
    }

    public boolean isIn(Category cat) {
        return categories.contains(cat);
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public boolean getPatch() {
        return patch;
    }

    public List<Category> getCategories() {
        return new ArrayList<Category>(categories);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return (o instanceof Feature)
            && ((Feature)o).getUrl().equals(getUrl());
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }
}
