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

import org.junit.Before;
import org.junit.Test;

import upside.site.CachingSiteLoader;
import upside.site.Site;
import upside.site.TestSiteLoader;
import static junit.framework.TestCase.*;

public class FederationManagerTest {

    Site expected = Site.load(getClass().getResource("/merged-site.xml"));
    TestSiteLoader tsl = new TestSiteLoader();
    FederationManager fm;

    @Before
    public void setUp() {
        fm = FederationManagerFactory.create("/upside-test.properties", "/merge-config.xml", tsl);
        tsl.sitesLoaded = 0;
        tsl.isUpToDate = false;
    }

    @Test
    public void testLoadProperties() {
        FederationManager fedman = FederationManagerFactory.create(
            "/upside-test.properties", "/merge-config.xml");
        assertEquals(99000, ((CachingSiteLoader)fedman.getSiteLoader()).getTimeToLive());
    }

    @Test
    public void testFederationManager() {

        federate(2);

        federate(4);

        tsl.isUpToDate = true;

        federate(4);
    }

    private Site federate(int expectedLoads) {
        Site federated = fm.getFederatedSite("merged");
        assertTrue(federated.deepEquals(expected));
        assertEquals(expectedLoads, tsl.sitesLoaded);
        return federated;
    }
}
