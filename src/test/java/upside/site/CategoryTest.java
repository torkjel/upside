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

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.*;

import org.junit.Test;

public class CategoryTest extends SiteTestBase {

    private Site s = loadSite();

    @Test
    public void testParse() {
        assertEquals(3, s.getCategories().size());

        Category c = s.getCategory("xyzzy");
        assertEquals("xyzzy", c.getName());
        assertEquals("Xyzzy plugins", c.getLabel());
        assertEquals("This is the Xyzzy plugins", c.getDescription());

        Category c2 = s.getCategory("kizzy");
        assertNull(c2.getDescription());
    }

    @Test
    public void testGetCategory() {
        assertEquals("xyzzy", s.getCategory("xyzzy").getName());
        assertNull(s.getCategory("nothing-here"));
    }

    @Test
    public void testHashEquals() {
        Category c1 = new Category("a", "b", "c");
        Category c2 = new Category("b", "b", "c");
        Category c3 = new Category("a", "d", "e");

        assertEquals(c1, c1);
        assertFalse(c1.equals(c2));
        assertEquals(c1, c3);
        assertEquals(c3, c1);

        assertEquals(c1.hashCode(), c3.hashCode());
        assertFalse(c1.hashCode() == c2.hashCode());
    }

    @Test
    public void testToString() {
        // don't crash or anything...
        assertNotNull(loadSite().getCategory("kizzy").toString());
    }
}
