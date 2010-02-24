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

    public Set<FederatedSite> getFederatedSites() {
        return new HashSet<FederatedSite>(federatedSites.values());
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
