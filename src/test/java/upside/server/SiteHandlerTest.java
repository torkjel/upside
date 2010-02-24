package upside.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Site;
import upside.site.TestSiteLoader;

public class SiteHandlerTest {

    @Test
    public void testSiteIndexHandler() {
        // generate the site.xml page, and validate that the
        // generated page parses correctly

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SiteHandler sh = new SiteHandler(out, "merged");
        FederationManager fm = FederationManagerFactory.create(
                "/upside-test.properties", "/merge-config.xml", new TestSiteLoader());
        sh.setFederationManager(fm);

        sh.handle();

        Site merged = Site.load(new ByteArrayInputStream(out.toByteArray()));

        assertTrue(merged.deepEquals(fm.getFederatedSite("merged")));

    }
}
