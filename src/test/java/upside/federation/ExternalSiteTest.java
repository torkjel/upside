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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class ExternalSiteTest extends FederationTestBase {

    @Test
    public void testExternalSites() throws MalformedURLException {
        Config conf = loadConf();

        assertNotNull(conf);

        assertEquals("http://upside.example.com/", conf.getBaseUrl());

        Set<String> names = conf.getOriginSiteNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("foo"));
        assertTrue(names.contains("bar"));

        for (String name : names) {
            if (name.equals("foo")) {
                URL u = new URL("http://foo.example.com/site/");
                assertEquals(u, conf.getOriginSiteUrl(name));
                assertEquals(u, conf.getOriginSites().get(name));
            } else {
                URL u = new URL("http://bar.example.com/update-site/");
                assertEquals(u, conf.getOriginSiteUrl(name));
                assertEquals(u, conf.getOriginSites().get(name));
            }
        }
    }
}
