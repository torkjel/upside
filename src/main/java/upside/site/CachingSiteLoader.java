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
package upside.site;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import upside.utils.Utils;

public class CachingSiteLoader implements SiteLoader {

    private Map<URL, SiteCache> cache = new HashMap<URL, SiteCache>();

    private int timeToLive;
    private SiteFactory siteFactory;

    public CachingSiteLoader(SiteFactory siteFactory, int timeToLive) {
        this.timeToLive = timeToLive;
        this.siteFactory = siteFactory;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public Site loadSite(URL url) {
        if (!isCached(url))
            load(url);
        return get(url);
    }

    @Override
    public boolean isUpToDate(URL url, long timestamp) {
        loadSite(url);
        return cache.get(url).lastModified <= timestamp;
    }

    private boolean isCached(URL url) {
        return cache.containsKey(url) && !cache.get(url).hasExpired(timeToLive);
    }

    private void load(URL url) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Utils.pipe(Site.urlToSiteXml(url).openStream(), baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = baos.toByteArray();
        String hash = Utils.hash(data);
        System.out.println("Loading site '" + url + "'");
        Site s = siteFactory.create(new ByteArrayInputStream(data)).withAbsoluteUrls(url);

        SiteCache sc = cache.get(url);
        cache.put(
            url,
            sc != null
                ? sc.update(s, hash)
                : new SiteCache(s, hash));
    }

    private Site get(URL url) {
        return cache.get(url).site;
    }

    private static class SiteCache {
        long loadTime;
        long lastModified;
        String hash;
        Site site;

        SiteCache(Site site, String hash) {
            init(site, hash);
        }

        SiteCache update(Site site, String hash) {
            init(site, hash);
            return this;
        }

        private void init(Site site, String hash) {
            this.loadTime = System.currentTimeMillis();
            if (!Utils.eq(this.hash, hash))
                lastModified = System.currentTimeMillis();
            this.site = site;
            this.hash = hash;
        }

        boolean hasExpired(int ttl) {
            return System.currentTimeMillis() > loadTime + ttl;
        }
    }

}
