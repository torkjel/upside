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

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.site.Site;
import upside.site.SiteLoader;
import upside.site.TestSiteLoader;

public class FederatorTest extends FederationTestBase {

    Site site1 = Site.load(getClass().getResourceAsStream("/site-to-merge-1.xml"));
    Site site2 = Site.load(getClass().getResourceAsStream("/site-to-merge-2.xml"));
    Config conf = new ConfigParser(getClass().getResourceAsStream("/merge-config.xml")).parse();
    SiteLoader loader = new TestSiteLoader();

    Site expected = Site.load(getClass().getResourceAsStream("/merged-site.xml"));

    @Test
    public void testFederateSites() {
        Federator f = new Federator();
        Site federated = f.federateSites("merged", loader, conf);
        assertNotNull(federated);
        assertTrue(expected.deepEquals(federated));
    }
}
