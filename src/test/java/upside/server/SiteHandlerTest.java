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
package upside.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Site;
import upside.site.TestSiteLoader;

public class SiteHandlerTest {

    @Test
    public void testSiteIndexHandler() {
        // generate the site.xml page, and validate that the
        // generated page parses correctly

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SiteHandler sh = new SiteHandler(out, "merged");
        FederationManager fm = FederationManagerFactory.create(
                "/upside-test.properties", "/merge-config.xml", new TestSiteLoader());
        sh.setFederationManager(fm);

        sh.handle();

        Site merged = Site.load(new ByteArrayInputStream(out.toByteArray()));

        assertTrue(merged.deepEquals(fm.getFederatedSite("merged")));

    }
}
