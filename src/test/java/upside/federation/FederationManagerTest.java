package upside.federation;

import org.junit.Before;
import org.junit.Test;

import upside.site.CachingSiteLoader;
import upside.site.Site;
import upside.site.TestSiteLoader;
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
}
