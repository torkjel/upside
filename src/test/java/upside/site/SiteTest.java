package upside.site;

import static junit.framework.TestCase.*;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class SiteTest extends SiteTestBase {

    @Test
    public void testLoad() {
        Site site = loadSite();
        assertNotNull(site);
    }

    @Test
    public void testParseDescription() {
        Site site = loadSite();
        assertEquals(URL, site.getDescription().getUrl());
        assertEquals("The Foo-Bar update site", site.getDescription().getDescription());
    }

    @Test
    public void testAbsoluteUrls() throws MalformedURLException {
        String feature = "features/foo_1.0.jar";
        String archivePath = "plugins/noo_1.3.jar";
        String u = "http://example.com/";
        URL url = new URL(u);

        Site rel = loadSite();
        Site abs = rel.withAbsoluteUrls(url);

        // absolute feature URLs
        assertNotNull(abs.getFeature(u + feature));
        assertNull(abs.getFeature(feature));

        // absolute archive URLs
        Archive a = abs.getArchive(archivePath);
        assertEquals(u + archivePath, a.getUrl());

        // unchanged stuff...
        assertEquals(rel.getDescription().getUrl(), abs.getDescription().getUrl());
        assertEquals(rel.getCategories(), abs.getCategories());
    }

    @Test
    public void testWithDescription() {
        Site a = loadSite();
        Site b = loadSite().withDescription("http://foo", "bar");
        assertEquals(b.getDescription().getUrl(), "http://foo");
        assertEquals(b.getDescription().getDescription(), "bar");
        assertEquals(a.getArchives(), b.getArchives());
        assertEquals(a.getCategories(), b.getCategories());
        assertEquals(a.getFeatures(), b.getFeatures());
    }

    @Test
    public void testMergeDescriptions() {
        Site a = loadSite();
        Site b = loadOtherSite();
        String url = "http://deathstar.mil";
        Site c = Site.merge(url, a, b);

        assertEquals(url, c.getDescription().getUrl());
        assertTrue(c.getDescription().getDescription().contains(a.getDescription().getUrl()));
        assertTrue(c.getDescription().getDescription().contains(b.getDescription().getUrl()));
    }

    @Test
    public void testMergeArchives() {
        Site a = loadSite();
        Site b = loadOtherSite();
        String url = "http://deathstar.mil";
        Site c = Site.merge(url, a, b);

        assertNotNull(c.getArchive("plugins/foo_1.0.jar"));
        assertNotNull(c.getArchive("plugins/bar_1.1.jar"));
        assertNotNull(c.getArchive("plugins/znoof_2.0.jar"));

        assertEquals(
            a.getArchives().size() + b.getArchives().size() -1,
            c.getArchives().size());
    }

    @Test
    public void testMergeCategories() {
        Site a = loadSite();
        Site b = loadOtherSite();
        String url = "http://deathstar.mil";
        Site c = Site.merge(url, a, b);

        assertNotNull(c.getCategory("xyzzy"));
        assertNotNull(c.getCategory("wizzy"));
        assertNotNull(c.getCategory("naff"));

        assertEquals(
            a.getCategories().size() + b.getCategories().size() -1,
            c.getCategories().size());
    }

    @Test
    public void testMergeFeatures() {
        Site a = loadSite();
        Site b = loadOtherSite();
        String url = "http://deathstar.mil";
        Site c = Site.merge(url, a, b);

        assertNotNull(c.getFeature("features/foo_1.0.jar"));
        assertNotNull(c.getFeature("features/bar_1.1.jar"));
        assertNotNull(c.getFeature("features/znoof_2.0.jar"));

        assertEquals(
                a.getFeatures().size() + b.getFeatures().size() -1,
                c.getFeatures().size());

        // test merging of features' categories.
        Feature bar = c.getFeature("features/bar_1.1.jar");
        assertTrue(bar.getCategories().contains("xyzzy"));
        assertTrue(bar.getCategories().contains("naff"));

        assertEquals(
            a.getFeature("features/bar_1.1.jar").getCategories().size() +
                b.getFeature("features/bar_1.1.jar").getCategories().size() -1,
            bar.getCategories().size());
    }

    @Test
    public void testToString() {
        Site s = loadSite();
        Site roundtrip = Site.load(new ByteArrayInputStream(s.toString().getBytes()));
        assertEquals(s, roundtrip);
    }
}
