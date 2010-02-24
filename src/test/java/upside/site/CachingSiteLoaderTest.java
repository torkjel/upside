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

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.junit.Test;

import upside.utils.Utils;
import static junit.framework.TestCase.*;

public class CachingSiteLoaderTest {

    TestSiteFactory sf = new TestSiteFactory();
    CachingSiteLoader csl;

    @org.junit.Before
    public void setUp() {
        sf.sitesCreated = 0;
        csl = new CachingSiteLoader(sf, 1000);
    }

    private URL url = getClass().getResource("/test-site.xml");

    private Site load() {
        Site s = csl.loadSite(url);
        assertNotNull(s);
        return s;
    }

    @Test
    public void testCaching() {

        load();
        assertEquals(1, sf.sitesCreated);

        // get from cache
        load();
        assertEquals(1, sf.sitesCreated);

        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        // cache should have expired
        load();
        assertEquals(2, sf.sitesCreated);
    }

    @Test
    public void testLastModified() throws UnsupportedEncodingException {

        // store site as temp file...
        File siteFile = Utils.saveAsTempFile(load().toStream());
        this.url = Utils.asUrl(siteFile);
        sf.sitesCreated = 0;

        load();
        assertEquals(1, sf.sitesCreated);
        long ts = System.currentTimeMillis();
        assertTrue(csl.isUpToDate(url, ts));

        try { Thread.sleep(10); } catch (InterruptedException e) { }

        // cached instance is always up to date
        load();
        assertEquals(1, sf.sitesCreated);
        assertTrue(csl.isUpToDate(url, ts));

        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        // unchanged instance is up to date
        Site s = load();
        assertEquals(2, sf.sitesCreated);
        assertTrue(csl.isUpToDate(url, ts));

        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        Site modified = s.withDescription("http://olle.molle", "modified site");
        Utils.saveFile(siteFile, modified.toStream());

        // modified version is NOT up to date.
        load();
        assertEquals(3, sf.sitesCreated);
        assertFalse(csl.isUpToDate(url, ts));
    }

    private class TestSiteFactory implements SiteFactory {
        int sitesCreated = 0;
        @Override
        public Site create(InputStream in) {
            sitesCreated ++;
            return Site.load(in);
        }
    }
}
