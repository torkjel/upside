package upside.federation;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import upside.site.CachingSiteLoader;
import upside.site.Site;
import upside.site.SiteLoader;
import static junit.framework.TestCase.*;

public class FederationManagerTest {

    Site expected = Site.load(getClass().getResource("/merged-site.xml"));
    TestSiteLoader tsl = new TestSiteLoader();
    FederationManager fm;

    @Before
    public void setUp() {
        fm = FederationManagerFactory.create("/upside-test.properties", "/merge-config.xml", tsl);
        tsl.sitesLoaded = 0;
        tsl.isUpToDate = false;
    }

    @Test
    public void testLoadProperties() {
        FederationManager fedman = FederationManagerFactory.create(
            "/upside-test.properties", "/merge-config.xml");
        assertEquals(99000, ((CachingSiteLoader)fedman.getSiteLoader()).getTimeToLive());
    }

    @Test
    public void testFederationManager() {

        federate(2);

        federate(4);

        tsl.isUpToDate = true;

        federate(4);
    }

    private Site federate(int expectedLoads) {
        Site federated = fm.getFederatedSite("merged");
        assertTrue(federated.deepEquals(expected));
        assertEquals(expectedLoads, tsl.sitesLoaded);
        return federated;
    }

    class TestSiteLoader implements SiteLoader {

        int sitesLoaded = 0;
        boolean isUpToDate = false;

        @Override
        public boolean isUpToDate(URL url, long timestamp) {
            return isUpToDate;
        }

        @Override
        public Site loadSite(URL url) {
            sitesLoaded++;
            if (url.toString().contains("site1"))
                return Site.load(getClass().getResource("/site-to-merge-1.xml"));
            else if (url.toString().contains("site2"))
                return Site.load(getClass().getResource("/site-to-merge-2.xml"));
            else
                throw new IllegalArgumentException(url.toString());
        }

    }
}
