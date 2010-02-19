package upside.site;

import static junit.framework.TestCase.*;

import org.junit.Test;

public class SiteTest extends SiteTestBase {

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
}
