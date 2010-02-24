package upside.federation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import upside.site.CachingSiteLoader;
import upside.site.SiteFactoryImpl;
import upside.site.SiteLoader;

public class FederationManagerFactory {

    private static FederationManager instance;

    public static FederationManager create(
            String propertiesResource,
            String configResource) {
        return create(propertiesResource, configResource, null);
    }

    public static FederationManager create(
            String propertiesResource,
            String configResource,
            SiteLoader siteLoader) {

        if (siteLoader == null) {
            int timeToLive = getTimeToLive(parseProperties(propertiesResource));
            siteLoader = new CachingSiteLoader(new SiteFactoryImpl(), timeToLive);
        }

        return new FederationManager(
            new Federator(),
            siteLoader,
            new ConfigParser(FederationManager.class.getResourceAsStream(configResource)).parse());
    }

    public static synchronized FederationManager getFederationManager() {
        return instance != null
            ? instance
            : (instance = create("/upside.properties", "/upside-conf.xml"));
    }

    private static Properties parseProperties(String propertiesResource) {
        InputStream in = FederationManagerFactory.class.getResourceAsStream(propertiesResource);
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try { in.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        return props;
    }

    private static int getTimeToLive(Properties props) {
        String ttl = props.getProperty("upside.cachedsite.timetolive");
        return (ttl != null ? Integer.valueOf(ttl) : 60 * 60) * 1000;
    }
}
