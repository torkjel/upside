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

import java.net.URL;

@org.junit.Ignore
public class TestSiteLoader implements SiteLoader {

    public int sitesLoaded = 0;
    public boolean isUpToDate = false;

    @Override
    public boolean isUpToDate(URL url, long timestamp) {
        return isUpToDate;
    }

    @Override
    public Site loadSite(URL url) {
        sitesLoaded++;
        if (url.toString().contains("site1"))
            return Site.load(getClass().getResource("/site-to-merge-1.xml"));
        else if (url.toString().contains("site2"))
            return Site.load(getClass().getResource("/site-to-merge-2.xml"));
        else
            throw new IllegalArgumentException(url.toString());
    }
}
