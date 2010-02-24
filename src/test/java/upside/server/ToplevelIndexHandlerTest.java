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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Site;
import upside.site.TestSiteLoader;

public class ToplevelIndexHandlerTest {

    @Test
    public void testSiteIndexHandler() throws UnsupportedEncodingException {
        // generate the top-level index.html page, and validate that the
        // content is somewhat correct.

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ToplevelIndexHandler tih = new ToplevelIndexHandler(out);
        FederationManager fm = FederationManagerFactory.create(
                "/upside-test.properties", "/merge-config.xml", new TestSiteLoader());
        tih.setFederationManager(fm);
        Site merged = fm.getFederatedSite("merged");

        tih.handle();

        String tlIndex = out.toString("UTF-8");

        // heuristic... check that the site's url is there.
        assertTrue(tlIndex.contains(merged.getDescription().getUrl()));
    }
}
