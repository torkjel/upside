package upside.federation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import upside.site.CachingSiteLoader;
import upside.site.Site;
import upside.site.SiteLoader;

public class FederationManager {

    //TODO: Only invalidate a cached federated site when an external site
    //      that is actually in use have been modified. For now the federated
    //      sites are invalidates once any external site has changed.

    Map<URL, Long> loadTimestamps = new HashMap<URL, Long>();
    Map<String, Site> federatedSites = new HashMap<String, Site>();

    private Config config;
    private Federator federator;
    private SiteLoader siteLoader;

    public FederationManager(Federator federator, SiteLoader siteLoader, Config config) {
        this.federator = federator;
        this.siteLoader = new SL(siteLoader);
        this.config = config;
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

        private SiteLoader sl;

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

    private static final FederationManager instance;

    static {
        Class<FederationManager> clazz = FederationManager.class;
        InputStream in = clazz.getResourceAsStream("/upside.properties");
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try { in.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        int timeToLive = Integer.valueOf(props.getProperty("upside.cachedsite.timetolive")) * 1000;
        instance = new FederationManager(
            new Federator(),
            new CachingSiteLoader(timeToLive),
            new ConfigParser(clazz.getResourceAsStream("/upside-conf.xml")).parse());
    }

    public static final FederationManager getinstance() {
        return instance;
    }
}
