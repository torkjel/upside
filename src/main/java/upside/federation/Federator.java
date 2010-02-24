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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import upside.site.Category;
import upside.site.Description;
import upside.site.Feature;
import upside.site.Site;
import upside.site.SiteLoader;

public class Federator {

    public Site federateSites(String name, SiteLoader loader, Config config) {

        FederatedSite fs = config.getFederatedSite(name);
        if (fs == null)
            return null;

        Site federated = null;
        for (String originSiteName : config.getOriginSiteNames()) {
            Site originSite = loader.loadSite(config.getOriginSiteUrl(originSiteName));
            Site extracted = extractFrom(originSiteName, originSite, fs);
            federated = Site.merge(
                config.getBaseUrl() + fs.getName(),
                extracted, federated);
        }
        return federated.withDescription(config.getBaseUrl() + fs.getName() + "/", fs.getDescription());
    }

    private Site extractFrom(String originSiteName, Site originSite, FederatedSite federatedSite) {
        Set<Category> categories = new HashSet<Category>();
        Map<String, Feature> features = new HashMap<String, Feature>();

        // handle federated-site level includes
        for (Include inc : federatedSite.getIncludes()) {
            if (!originSiteName.equals(inc.getSite()))
                continue;
            Set<Feature> matches = inc.match(originSite);
            for (Feature f : matches) {
                addFeature(f, null, inc.getKeepCategories(), features);
                if (inc.getKeepCategories())
                    addCategories(f, categories, originSite);
            }
        }

        // handle category level includes
        for (FederatedCategory fc : federatedSite.getCategories()) {
            Category cat = new Category(fc.getName(), fc.getName(), fc.getDescription());
            categories.add(cat);
            for (Include inc : fc.getIncludes()) {
                if (!originSiteName.equals(inc.getSite()))
                    continue;
                Set<Feature> matches = inc.match(originSite);
                for (Feature f : matches) {
                    addFeature(f, cat.getName(), inc.getKeepCategories(), features);
                    if (inc.getKeepCategories())
                        addCategories(f, categories, originSite);
                }
            }
        }

        // dummy description will be discarded later, needs to be a valid url though.
        return new Site(
            new Description("http://foo.bar", ""),
            categories,
            new HashSet<Feature>(features.values()),
            originSite.getArchives());
    }

    private void addFeature(
            Feature f, String category,
            boolean keepCategories, Map<String, Feature> features) {

        Set<String> categories = new HashSet<String>();
        if (category != null)
            categories.add(category);
        if (keepCategories)
            categories.addAll(f.getCategories());

        String url = f.getUrl();

        if (!features.containsKey(url)) {
            features.put(url, f.moveToCategories(categories));
        } else
            features.put(url, features.get(url).withCategories(categories));
    }

    private void addCategories(Feature f, Set<Category> categories, Site originSite) {
        for (String catName : f.getCategories())
            categories.add(originSite.getCategory(catName));
    }
}
