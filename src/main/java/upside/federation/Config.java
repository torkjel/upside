package upside.federation;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {

    private String baseUrl;
    private Map<String, URL> originSties = new HashMap<String, URL>();
    private Map<String, FederatedSite> federatedSites = new HashMap<String, FederatedSite>();

    public Config(String baseUrl, Map<String, URL> originSites, Set<FederatedSite> federatedSites) {
        this.baseUrl = baseUrl;
        this.originSties = new HashMap<String, URL>(originSites);
        for (FederatedSite fs : federatedSites)
            this.federatedSites.put(fs.getName(), fs);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public URL getOriginSiteUrl(String name) {
        return originSties.get(name);
    }

    public FederatedSite getFederatedSite(String name) {
        return federatedSites.get(name);
    }

    public Set<String> getOriginSiteNames() {
        return new HashSet<String>(originSties.keySet());
    }

    public Map<String, URL> getOriginSites() {
        return new HashMap<String, URL>(originSties);
    }
}
