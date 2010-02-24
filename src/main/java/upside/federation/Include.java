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

    public abstract Set<Feature> match(Site site);

    public static class SiteInclude extends Include {

        private SiteInclude(String site, boolean keepCategories) {
            super(site, keepCategories);
        }

        @Override
        public Set<Feature> match(Site site) {
            return site.getFeatures();
        }

        @Override
        public String toString() {
            return getClass().getName() + ":[" +
                "site:" + getSite() +", " +
                "keekCategories:" + getKeepCategories() + "]";

        }
    }

    public static class FeatureInclude extends Include {
        private String feature;
        private String version;

        private FeatureInclude(String site, boolean keepCategories, String feature, String version) {
            super(site, keepCategories);
            this.feature = feature;
            this.version = version;
        }

        @Override
        public Set<Feature> match(Site site) {
            Set<Feature> matching = new HashSet<Feature>();
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

        public String getFeature() {
            return feature;
        }

        public String getVersion() {
            return version;
        }

    }

    public static class CategoryInclude extends Include {
        private String category;

        private CategoryInclude(String site, boolean keepCategories, String category) {
            super(site, keepCategories);
            this.category = category;
        }

        @Override
        public Set<Feature> match(Site site) {
            Set<Feature> matching = new HashSet<Feature>();
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

        public String getCategory() {
            return category;
        }
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

