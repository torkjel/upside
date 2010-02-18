package upside.federation;

import java.util.HashSet;
import java.util.Set;

import upside.site.Category;
import upside.site.Feature;
import upside.site.Site;

abstract class Include {

    private String site;
    private boolean keepCategories;

    private Include(String site, boolean keepCategories) {
        this.site = site;
        this.keepCategories = keepCategories;
    }

    public String getSite() {
        return site;
    }

    public boolean getKeepCategories() {
        return keepCategories;
    }

    @Override
    public abstract String toString();

    public abstract Set<Feature> match(String siteName, Site site);

    private static class SiteInclude extends Include {

        public SiteInclude(String site, boolean keepCategories) {
            super(site, keepCategories);
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            return getSite().equals(siteName)
                ? site.getFeatures()
                : new HashSet<Feature>();
        }

        @Override
        public String toString() {
            return getClass().getName() + ":[" +
                "site:" + getSite() +", " +
                "keekCategories:" + getKeepCategories() + "]";

        }
    }

    private static class FeatureInclude extends Include {
        private String feature;
        private String version;

        FeatureInclude(String site, boolean keepCategories, String feature, String version) {
            super(site, keepCategories);
            this.feature = feature;
            this.version = version;
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            Set<Feature> matching = new HashSet<Feature>();
            if (!siteName.equals(getSite()))
                return matching;
            for (Feature f : site.getFeatures())
                // TODO: proper version matching, including wildcard support.
                if (f.getId().equals(feature) &&
                    (version == null || version.equals(f.getVersion())))
                    matching.add(f);
            return matching;
        }

        @Override
        public String toString() {
            return getClass().getName() + ":[" +
                "site:" + getSite() +", " +
                "keekCategories:" + getKeepCategories() + ", " +
                "feature:" + feature + ", " +
                "version:" + version + "]";
        }
    }

    private static class CategoryInclude extends Include {
        private String category;

        private CategoryInclude(String site, boolean keepCategories, String category) {
            super(site, keepCategories);
            this.category = category;
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            Set<Feature> matching = new HashSet<Feature>();
            if (!siteName.equals(getSite()))
                return matching;
            Category cat = site.getCategory(category);
            if (cat == null)
                return matching;
            for (Feature f : site.getFeatures())
                if (f.isIn(cat))
                    matching.add(f);
            return matching;
        }

        @Override
        public String toString() {
            return getClass().getName() + ":[" +
                "site:" + getSite() +", " +
                "keekCategories:" + getKeepCategories() + ", " +
                "category:" + category + "]";
        }
    }

    public static IncludeBuilder newInclude(String name) {
        return newInclude(name, false);
    }

    public static IncludeBuilder newInclude(String name, boolean keepCategories) {
        return new IncludeBuilder(name, keepCategories);
    }

    public static class IncludeBuilder {
        private String name;
        private boolean keepCategories;

        public IncludeBuilder(String name, boolean keepCategories) {
            this.name = name;
            this.keepCategories = keepCategories;
        }

        public Include category(String category) {
            return new CategoryInclude(name, keepCategories, category);
        }

        public Include site() {
            return new SiteInclude(name, keepCategories);
        }

        public Include feature(String featureId, String version) {
            return new FeatureInclude(name, keepCategories, featureId, version);
        }

    }

}

