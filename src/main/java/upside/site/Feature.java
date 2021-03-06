/*
 * Copyright 2010 Torkjel Hongve (torkjelh@conduct.no)
 *
 * This file is part of Upside.
 *
 * Upside is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Upside is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Upside.  If not, see <http://www.gnu.org/licenses/>.
 */
package upside.site;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import upside.utils.Utils;

public class Feature extends AbstractSiteElement {
    private String url;
    private String id;
    private String version;
    private boolean patch = false;
    private Set<String> categories = new HashSet<String>();

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
        return maybeEquals(o) && ((Feature)o).getUrl().equals(getUrl());
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

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Feature f = (Feature)se;
            return eq(getUrl(), f.getUrl())
                && eq(getId(), f.getId())
                && eq(getCategories(), f.getCategories())
                && getPatch() == f.getPatch()
                && eq(getVersion(), f.getVersion());
        }
        return false;
    }
}


