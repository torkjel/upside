package upside.federation;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class FederatedSiteTest extends FederationTestBase {

    @Test
    public void testFederatedSites() {
        Config conf = loadConf();

        FederatedSite main = conf.getFederatedSite("main");
        assertNotNull(main);

        assertEquals("main", main.getName());
        assertEquals("Main site", main.getDescription());

        FederatedSite test = conf.getFederatedSite("test");
        assertNotNull(test);
        assertEquals("test", test.getName());
        assertEquals(null, test.getDescription());
    }
}
