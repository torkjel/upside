package upside.site;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

import org.junit.Test;

public class SiteTest {

    private static final String URL = "http://foo.bar/";

    private Site loadSite() {
        try {
            return Site.load(
                new URL(URL),
                getClass().getResourceAsStream("/test-site.xml"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoadSite() {
        Site site = loadSite();
        assertNotNull(site);
    }

    @Test
    public void testSiteParseDescription() {
        Site site = loadSite();
        assertEquals(URL, site.getDescription().getUrl().toString());
        assertEquals("The Foo-Bar update site", site.getDescription().getDescription());
    }

    @Test
    public void testParseFeatures() {
        Site site = loadSite();
        Set<Feature> features = site.getFeatures();
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
    public void testModifyFeatureURL() throws MalformedURLException {
        String furl = "features/foo_1.0.jar";
        Feature f = loadSite().getFeature(furl);
        f = f.withAbsoluteUrl(new URL(URL));
        assertEquals(URL + furl, f.getUrl());
    }

    @Test
    public void testModifyFeatureCategories() throws MalformedURLException {
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
    public void testFeatureHashEquals() {
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
        assertNotNull(
            loadSite().getFeature("features/zoo_1.1.jar").toString());
    }

    private Category cat(String name) {
        return new Category(name, name, name);
    }

    private Feature feat(String id, String version) {
        return new Feature(
            "feature/" + id + "_" + version + ".jar",
            id, version,
            false, new HashSet<Category>());
    }

}
