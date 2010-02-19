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
    public void testMerge() {
    }

    @Test
    public void testToString() {
        Site s = loadSite();
        Site roundtrip = Site.load(new ByteArrayInputStream(s.toString().getBytes()));
        assertEquals(s, roundtrip);
    }
}
