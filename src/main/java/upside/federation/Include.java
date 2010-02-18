package upside.federation;

import java.util.HashSet;
import java.util.Set;

import upside.site.Category;
import upside.site.Feature;
import upside.site.Site;

abstract class Include {

    private Include(String site) {
        this.site = site;
    }

    protected String site;

    public static Include createFeatureInclude(String site, String feature, String version) {
        return new FeatureInclude(site, feature, version);
    }

    public static Include createCategoryInclude(String site, String category) {
        return new CategoryInclude(site, category);
    }

    public static Include createSiteInclude(String site) {
        return new SiteInclude(site);
    }

    public abstract Set<Feature> match(String siteName, Site site);

    private static class SiteInclude extends Include {

        public SiteInclude(String site) {
            super(site);
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            return this.site.equals(siteName)
                ? site.getFeatures()
                : new HashSet<Feature>();
        }
    }

    private static class FeatureInclude extends Include {
        private String feature;
        private String version;

        FeatureInclude(String site, String feature, String version) {
            super(site);
            this.feature = feature;
            this.version = version;
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            Set<Feature> matching = new HashSet<Feature>();
            if (!siteName.equals(this.site))
                return matching;
            for (Feature f : site.getFeatures())
                // TODO: proper version matching, including wildcard support.
                if (f.getId().equals(feature) &&
                    (version == null || version.equals(f.getVersion())))
                    matching.add(f);
            return matching;
        }
    }

    private static class CategoryInclude extends Include {
        private String category;

        CategoryInclude(String site, String category) {
            super(site);
            this.category = category;
        }

        @Override
        public Set<Feature> match(String siteName, Site site) {
            Set<Feature> matching = new HashSet<Feature>();
            if (!siteName.equals(this.site))
                return matching;
            Category cat = site.getCategory(category);
            if (cat == null)
                return matching;
            for (Feature f : site.getFeatures())
                if (f.isIn(cat))
                    matching.add(f);
            return matching;
        }
    }

}
