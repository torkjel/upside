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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import upside.utils.XmlBuilder;

public class Site extends AbstractSiteElement {

    public static final String SITEXML = "site.xml";

    private Description description;
    private Set<Category> categories;
    private Set<Feature> features;
    private Set<Archive> archives;

    public Site(Description description, Set<Category> categories,
            Set<Feature> features, Set<Archive> archives) {
        this.description = description;
        this.categories = new HashSet<Category>(categories);
        this.features = new HashSet<Feature>(features);
        this.archives = new HashSet<Archive>(archives);
    }

    public Description getDescription() {
        return description;
    }

    public Set<Archive> getArchives() {
        return new HashSet<Archive>(archives);
    }

    public Archive getArchive(String path) {
        for (Archive a : getArchives())
            if (a.getPath().equals(path))
                return a;
        return null;
    }

    public Set<Category> getCategories() {
        return new HashSet<Category>(categories);
    }

    public Category getCategory(String name) {
        for (Category c : categories)
            if (c.getName().equals(name))
                return c;
        return null;
    }

    public Feature getFeature(String url) {
        for (Feature f : getFeatures())
            if (f.getUrl().equals(url))
                return f;
        return null;
    }

    public Set<Feature> getFeatures() {
        return new HashSet<Feature>(features);
    }

    public Set<Feature> getFeaturesIn(Category category) {
        Set<Feature> featuresInCat = new HashSet<Feature>();
        for (Feature f : features)
            if (f.isIn(category))
                featuresInCat.add(f);
        return featuresInCat;
    }

    public Set<Feature> getFeaturesWithoutCategory() {
        Set<Feature> featuresWOC = new HashSet<Feature>();
        for (Feature f : features)
            if (f.getCategories().isEmpty())
                featuresWOC.add(f);
        return featuresWOC;
    }

    public static URL urlToSiteXml(URL base) {
        if (base.getPath().endsWith("/"))
            try {
                base = new URL(base, SITEXML);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        return base;
    }

    public static Site load(URL url) {
        url = urlToSiteXml(url);
        try {
            return load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Site load(InputStream in) {
        return new SiteParser(in).parse();
    }

    public Site withAbsoluteUrls(URL origin) {
        Set<Feature> features = new HashSet<Feature>();
        for (Feature f : this.features)
            features.add(f.withAbsoluteUrl(origin));
        Set<Archive> archives = new HashSet<Archive>();
        for (Archive a : this.archives) {
            archives.add(a.hasAbsoluteUrl() ? a : a.withAbsoluteUrl(origin));
        }
        return new Site(description, categories, features, archives);
    }

    public Site withDescription(String url, String description) {
        return new Site(
            new Description(url, description),
            getCategories(), getFeatures(), getArchives());
    }

    public static Site merge(String newOrigin, Site ... sites) {
        StringBuilder sb = new StringBuilder("Merged sites:\n ");
        Set<Category> categories = new HashSet<Category>();
        Map<String, Feature> features = new HashMap<String, Feature>();
        Set<Archive> archives = new HashSet<Archive>();
        for (Site s : sites) {
            if (s == null) continue;
            String siteUrl = s.description.getUrl();
            sb.append(siteUrl + "\n");
            categories.addAll(s.categories);
            archives.addAll(s.archives);
            for (Feature f : s.features) {
                String url = f.getUrl();
                if (!features.containsKey(url))
                    features.put(url, f);
                else {
                    // if feature is already present, merge its categories
                    Feature feature = features.get(url);
                    features.put(url, feature.withCategories(f.getCategories()));
                }
            }
        }
        return new Site(
            new Description(newOrigin, sb.toString()),
            categories, new HashSet<Feature>(features.values()), archives);
    }

    public String toString() {
        XmlBuilder xml = new XmlBuilder();
        xml.open("site");
        xml.open("description");
        xml.attr("url", description.getUrl());
        if (description.getDescription() != null)
            xml.text(description.getDescription());
        xml.close();
        for(Feature f : features) {
            xml.open("feature");
            xml.attr("url", f.getUrl());
            xml.attr("id", f.getId());
            xml.attr("patch", f.getPatch());
            xml.attr("version", f.getVersion());
            for (String cat : f.getCategories())
                xml.open("category").attr("name", cat).close();
            xml.close();
        }
        for (Archive a : archives)
            xml.open("archive").attr("path", a.getPath()).attr("url", a.getUrl()).close();
        for (Category c : categories) {
            xml.open("category-def").attr("name", c.getName()).attr("label", c.getLabel());
            if (c.getDescription() != null)
                xml.open("description").text(c.getDescription()).close();
            xml.close();
        }
        xml.close();
        return xml.toString();
    }

    public InputStream toStream() {
        try {
            return new ByteArrayInputStream(toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (maybeEquals(o)) {
            Site s = (Site)o;
            return s.getDescription().equals(getDescription())
                && s.getArchives().equals(getArchives())
                && s.getCategories().equals(getCategories())
                && s.getFeatures().equals(getFeatures());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDescription().hashCode();
    }

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Site s = (Site)se;
            return getDescription().deepEquals(s.getDescription())
                && deepEquals(getArchives(), s.getArchives())
                && deepEquals(getCategories(), s.getCategories())
                && deepEquals(getFeatures(), s.getFeatures());
        }
        return false;
    }

    private <T extends SiteElement> boolean deepEquals(Set<T> set1, Set<T> set2) {
        if (set1.size() != set2.size())
            return false;
        Map<T, T> map = new HashMap<T, T>();
        for (T t : set1)
            map.put(t, t);
        for (T t : set2) {
            T u = map.get(t);
            if (u == null)
                return false;
            if (!t.deepEquals(u))
                return false;
        }
        return true;
    }
}
