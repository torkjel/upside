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
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import upside.federation.Include.CategoryInclude;
import upside.federation.Include.FeatureInclude;
import upside.federation.Include.SiteInclude;
import upside.site.Category;
import upside.site.Feature;
import upside.site.Site;
import static junit.framework.TestCase.*;

public class IncludeTest extends FederationTestBase {

    @Test
    public void testIncludes() throws MalformedURLException {
        Config conf = loadConf();

        Set<Include> includes = conf.getFederatedSite("main").getIncludes();
        assertEquals(3, includes.size());

        int cs = 0;
        for (Include inc : includes) {
            if (inc instanceof SiteInclude) {
                assertEquals("foo", inc.getSite());
                assertTrue(inc.getKeepCategories());
                cs += 1;
            } else if (inc instanceof CategoryInclude) {
                CategoryInclude ci = (CategoryInclude)inc;
                assertEquals("bar", ci.getSite());
                assertFalse(ci.getKeepCategories());
                assertEquals("barc", ci.getCategory());
                cs += 2;
            } else if (inc instanceof FeatureInclude) {
                FeatureInclude fi = (FeatureInclude)inc;
                assertEquals("foo", fi.getSite());
                assertFalse(fi.getKeepCategories());
                assertEquals("foo", fi.getFeature());
                assertEquals("1.0", fi.getVersion());
                cs += 4;
            } else
                cs += 8;
        }
        assertEquals(cs, 7);
    }

    @Test
    public void testToString() throws MalformedURLException {
        Set<Include> includes = loadConf().getFederatedSite("main").getIncludes();

        for (Include inc : includes)
            assertNotNull(inc.toString());
    }

    @Test
    public void testMatch() {
        Set<Include> includes = loadConf().getFederatedSite("main").getIncludes();
        Site s = Site.load(getClass().getResourceAsStream("/test-match-site.xml"));

        for (Include inc : includes) {
            if (inc instanceof SiteInclude) {
                assertEquals(s.getFeatures(), inc.match(s));
            } else if (inc instanceof CategoryInclude) {
                Set<Feature> features = inc.match(s);
                assertEquals(2, features.size());
                assertTrue(features.contains(new Feature("2", "", "", false, new HashSet<Category>())));
                assertTrue(features.contains(new Feature("3", "", "", false, new HashSet<Category>())));
            } else if (inc instanceof FeatureInclude) {
                Set<Feature> features = inc.match(s);
                assertEquals(1, features.size());
                assertTrue(features.contains(new Feature("1", "", "", false, new HashSet<Category>())));
            }
        }
    }
}
