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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.*;

public class FeatureTest extends SiteTestBase {

    private Site s = loadSite();

    @Test
    public void testParse() {
        Set<Feature> features = s.getFeatures();
        assertEquals(3, features.size());
        int checkSum = 0;
        for (Feature f : features) {
            if (f.getId().equals("foo")) {
                assertFalse(f.getPatch());
                assertEquals("features/foo_1.0.jar", f.getUrl());
                assertEquals("1.0", f.getVersion());
                assertTrue(f.getCategories().isEmpty());
                checkSum += 1;
            } else if (f.getId().equals("bar")) {
                assertEquals(1, f.getCategories().size());
                assertEquals("xyzzy", f.getCategories().iterator().next());
                checkSum += 2;
            } else if (f.getId().equals("zoo")) {
                assertTrue(f.getPatch());
                assertEquals(2, f.getCategories().size());
                assertTrue(f.getCategories().contains("xyzzy"));
                assertTrue(f.getCategories().contains("wizzy"));
                checkSum += 4;
            }
        }
        assertEquals(7, checkSum);
    }

    @Test
    public void testGetFeature() {
        assertEquals("foo", s.getFeature("features/foo_1.0.jar").getId());
        assertNull(s.getFeature("nothing-here"));
    }

    @Test
    public void testGetFeaturesIn() {
        Set<Feature> xy = s.getFeaturesIn(s.getCategory("xyzzy"));
        assertEquals(2, xy.size());
        assertTrue(xy.contains(s.getFeature("features/bar_1.1.jar")));
        assertTrue(xy.contains(s.getFeature("features/zoo_1.2.jar")));

        assertTrue(s.getFeaturesIn(s.getCategory("kizzy")).isEmpty());
    }

    @Test
    public void testModifyURL() throws MalformedURLException {
        String furl = "features/foo_1.0.jar";
        Feature f = loadSite().getFeature(furl);
        f = f.withAbsoluteUrl(new URL(URL));
        assertEquals(URL + furl, f.getUrl());
    }

    @Test
    public void testModifyCategories() throws MalformedURLException {
        String furl = "features/foo_1.0.jar";

        // add a category
        Category cat1 = cat("cat1");
        Feature f = loadSite().getFeature(furl);
        assertFalse(f.isIn(cat1));
        f = f.withCategories(Collections.singleton(cat1.getName()));
        assertTrue(f.isIn(cat1));

        // add another category
        Category cat2 = cat("cat2");
        assertFalse(f.isIn(cat2));
        f = f.withCategories(Collections.singleton(cat2.getName()));
        assertEquals(2, f.getCategories().size());
        assertTrue(f.isIn(cat1));
        assertTrue(f.isIn(cat2));

        // move to 3rd category
        Category cat3 = cat("cat3");
        assertFalse(f.isIn(cat3));
        f = f.moveToCategories(Collections.singleton(cat3.getName()));
        assertEquals(1, f.getCategories().size());
        assertFalse(f.isIn(cat1));
        assertFalse(f.isIn(cat2));
        assertTrue(f.isIn(cat3));
    }

    @Test
    public void testHashEquals() {
        Feature f = feat("test", "1");
        Feature f2 = feat("TEST", "1");
        Feature f3 = feat("test", "2");
        Feature f4 =  new Feature(f.getUrl(), "foo", "99", true, new HashSet<Category>());

        assertEquals(f, f);
        assertFalse(f.equals(f2));
        assertFalse(f.equals(f3));
        assertEquals(f, f4);

        assertTrue(f.hashCode() != f2.hashCode());
        assertTrue(f.hashCode() != f3.hashCode());
        assertEquals(f.hashCode(), f4.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(s.getFeature("features/zoo_1.2.jar").toString());
    }

}

