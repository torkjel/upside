package upside.site;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import upside.utils.Utils;

public class Feature {
    private String url;
    private String id;
    private String version;
    private boolean patch = false;
    private Set<String> categories = new HashSet<String>();

    public Feature(String url, String id, String version, boolean patch, Category category) {
        this(url, id, version, patch, Collections.singletonList(category));
    }

    public Feature(String url, String id, String version, boolean patch, Collection<Category> categories) {
        this(url, id, version, patch);
        for (Category cat : categories)
            this.categories.add(cat.getName());
    }

    public Feature(String url, String id, String version, boolean patch, Set<String> categories) {
        this(url, id, version, patch);
        this.categories = new HashSet<String>(categories);
    }

    private Feature(String url, String id, String version, boolean patch) {
        this.url = url;
        this.id = id;
        this.version = version;
        this.patch = patch;
    }

    public Feature withAbsoluteUrl(URL origin) {
        return new Feature(Utils.absoluteUrl(origin, url), id, version, patch, categories);
    }

    public Feature withCategories(Set<String> newCategories) {
        Set<String> categories = new HashSet<String>(this.categories);
        categories.addAll(newCategories);
        return new Feature(url, id, version, patch, categories);
    }

    public Feature moveToCategories(Set<String> categories) {
        return new Feature(url, id, version, patch, categories);
    }

    public boolean isIn(Category cat) {
        return categories.contains(cat.getName());
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

    public Set<String> getCategories() {
        return new HashSet<String>(categories);
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

    @Override
    public String toString() {
        return getClass().getName() + ":[" +
            id + "/" + version + ", " +
            "url:" + url + ", " +
            "patch:" + patch + ", " +
            "categories:" + categories + "]";
    }
}
