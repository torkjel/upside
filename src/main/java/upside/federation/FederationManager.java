package upside.federation;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import upside.site.Site;
import upside.site.SiteLoader;

public class FederationManager {

    //TODO: Only invalidate a cached federated site when an external site
    //      that is actually in use have been modified. For now the federated
    //      sites are invalidates once any external site has changed.

    private Map<URL, Long> loadTimestamps = new HashMap<URL, Long>();
    private Map<String, Site> federatedSites = new HashMap<String, Site>();

    private Config config;
    private Federator federator;
    private SL siteLoader;

    public FederationManager(Federator federator, SiteLoader siteLoader, Config config) {
        this.federator = federator;
        this.siteLoader = new SL(siteLoader);
        this.config = config;
    }

    // needed for testing.
    SiteLoader getSiteLoader() {
        return siteLoader.sl;
    }

    public Config getConfig() {
        return config;
    }

    public Site getFederatedSite(String name) {
        System.out.println("Federating site '" + name + "'");
        Site federatedSite = getCachedFederatedSite(name);
        if (federatedSite == null) {
            federatedSite = federator.federateSites(name, siteLoader, config);
            federatedSites.put(name, federatedSite);
        } else {
            System.out.println("Using cached instance of '" + name + "'");
        }
        return federatedSite;
    }

    public Map<String, Site> getOriginSites(String name) {
        Map<String, Site> allSites = new HashMap<String, Site>();
        for (String originSite : config.getOriginSiteNames())
            allSites.put(
                originSite,
                siteLoader.loadSite(config.getOriginSiteUrl(originSite)));
        return allSites;
    }

    private Site getCachedFederatedSite(String name) {
        if (!federatedSites.containsKey(name))
            return null;
        for (String originSiteName : config.getOriginSiteNames()) {
            URL url = config.getOriginSiteUrl(originSiteName);
            if (!loadTimestamps.containsKey(url))
                return null;
            if (!siteLoader.isUpToDate(url, loadTimestamps.get(url))) {
                return null;
            }
        }
        return federatedSites.get(name);
    }

    private class SL implements SiteLoader {

        SiteLoader sl;

        SL(SiteLoader sl) {
            this.sl = sl;
        }

        @Override
        public boolean isUpToDate(URL url, long timestamp) {
            return sl.isUpToDate(url, timestamp);
        }

        @Override
        public Site loadSite(URL url) {
            Site s = sl.loadSite(url);
            loadTimestamps.put(url, System.currentTimeMillis());
            return s;
        }
    }
}
