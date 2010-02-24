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

import java.util.HashSet;

public abstract class SiteTestBase {

    protected static final String URL = "http://foo.bar/";

    protected static final String SITE_RESOURCE = "/test-site.xml";
    protected static final java.net.URL SITE_URL = SiteTestBase.class.getResource(SITE_RESOURCE);

    private Site site;
    private Site site2;

    protected synchronized Site loadSite() {
        return site == null ? site = loadSite(SITE_RESOURCE) : site;
    }

    protected Site loadOtherSite() {
        return site2 == null ? site2 = loadSite("/test-site2.xml") : site2;
    }

    private Site loadSite(String resource) {
        return Site.load(getClass().getResource(resource));
    }

    protected Category cat(String name) {
        return new Category(name, name, name);
    }

    protected Feature feat(String id, String version) {
        return new Feature(
            "feature/" + id + "_" + version + ".jar",
            id, version,
            false, new HashSet<Category>());
    }

}
