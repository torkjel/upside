package upside.server;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Site;
import upside.site.TestSiteLoader;

public class ToplevelIndexHandlerTest {

    @Test
    public void testSiteIndexHandler() throws UnsupportedEncodingException {
        // generate the top-level index.html page, and validate that the
        // content is somewhat correct.

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ToplevelIndexHandler tih = new ToplevelIndexHandler(out);
        FederationManager fm = FederationManagerFactory.create(
                "/upside-test.properties", "/merge-config.xml", new TestSiteLoader());
        tih.setFederationManager(fm);
        Site merged = fm.getFederatedSite("merged");

        tih.handle();

        String tlIndex = out.toString("UTF-8");

        // heuristic... check that the site's url is there.
        assertTrue(tlIndex.contains(merged.getDescription().getUrl()));
    }
}
