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
