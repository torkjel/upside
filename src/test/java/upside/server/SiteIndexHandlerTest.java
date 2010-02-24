package upside.server;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Category;
import upside.site.Feature;
import upside.site.Site;
import upside.site.TestSiteLoader;

public class SiteIndexHandlerTest {

    @Test
    public void testSiteIndexHandler() throws UnsupportedEncodingException {
        // generate the top-level index.html page, and validate that the
        // content is somewhat correct.

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SiteIndexHandler sih = new SiteIndexHandler(out, "merged");
        FederationManager fm = FederationManagerFactory.create(
                "/upside-test.properties", "/merge-config.xml", new TestSiteLoader());
        sih.setFederationManager(fm);
        Site merged = fm.getFederatedSite("merged");

        sih.handle();

        String sIndex = out.toString("UTF-8");

        // heuristic... check that the site's features and categories are there.
        for (Category c : merged.getCategories())
            assertTrue(c.toString(), sIndex.contains(c.getLabel()));
        for (Feature f : merged.getFeatures()) {
            assertTrue(f.toString(), sIndex.contains(f.getId()));
            assertTrue(f.toString(), sIndex.contains(f.getVersion()));
        }
    }
}
