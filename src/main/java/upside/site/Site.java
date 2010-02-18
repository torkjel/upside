package upside.site;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import upside.utils.Exceptions;
import upside.utils.XmlBuilder;

public class Site {

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

    public Set<Archive> getArchives() {
        return new HashSet<Archive>(archives);
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

    public static Site load(URL url) {
        try {
            return load(url, new URL(url, SITEXML).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Site load(URL origin, InputStream in) {
        return new SiteParser(in).parse();
    }

    public Site withAbsoluteUrls(URL origin) {
        Set<Feature> features = new HashSet<Feature>();
        for (Feature f : this.features)
            features.add(f.withAbsoluteUrl(origin));
        Set<Archive> archives = new HashSet<Archive>();
        for (Archive a : this.archives)
            archives.add(a.withAbsoluteUrl(origin));
        return new Site(description, categories, features, archives);
    }

    public Site withDescription(String url, String description) {
        return new Site(
            new Description(url, description),
            getCategories(), getFeatures(), getArchives());
    }

    public static Site merge(String newOrigin, Site ... sites) {
        try {
            return merge(new URL(newOrigin), sites);
        } catch (MalformedURLException e) {
            throw Exceptions.re(e);
        }
    }

    public static Site merge(URL newOrigin, Site ... sites) {
        StringBuilder sb = new StringBuilder("Merged sites:\n ");
        Set<Category> categories = new HashSet<Category>();
        Set<Feature> features = new HashSet<Feature>();
        Set<Archive> archives = new HashSet<Archive>();
        for (Site s : sites) {
            if (s == null) continue;
            String siteUrl = s.description.getUrl().toString();
            sb.append(siteUrl + "\n");
            // TODO: handle duplicates!
            categories.addAll(s.categories);
            features.addAll(s.features);
            archives.addAll(s.archives);
        }
        return new Site(
            new Description(newOrigin, sb.toString()),
            categories, features, archives);
    }

    public String toString() {
        XmlBuilder xml = new XmlBuilder();
        xml.open("site");
        xml.open("description");
        xml.attr("url", description.getUrl().toString());
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
}
