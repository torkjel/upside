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

    private SiteLoader loader;
    private Config config;

    public Federator(SiteLoader siteLoader, Config config) {
        this.loader = siteLoader;
        this.config = config;
    }

    public Site federateSites(String name) {

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
        return new Site(
                new Description(config.getBaseUrl() + fs.getName(), fs.getDescription()),
                federated.getCategories(),
                federated.getFeatures(),
                federated.getArchives());
    }

    private Site extractFrom(String originSiteName, Site originSite, FederatedSite federatedSite) {
        Set<Category> categories = new HashSet<Category>();
        Map<String, Feature> features = new HashMap<String, Feature>();

        for (FederatedCategory fc : federatedSite.getCategories()) {
            Category cat = new Category(fc.getName(), fc.getName(), fc.getDescription());
            categories.add(cat);
            for (Include inc : fc.getIncludes()) {
                Set<Feature> matches = inc.match(originSiteName, originSite);
                for (Feature f : matches) {
                    String url = f.getUrl();
                    if (!features.containsKey(url)) {
                        features.put(url, f.moveToCategory(cat));
                    } else
                        features.put(url, features.get(url).withCategory(cat));
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
}
