package upside.federation;

import java.net.URL;

import org.junit.Test;
import static junit.framework.TestCase.*;

import upside.site.Site;
import upside.site.SiteLoader;

public class FederatorTest extends FederationTestBase {

    Site site1 = Site.load(getClass().getResourceAsStream("/site-to-merge-1.xml"));
    Site site2 = Site.load(getClass().getResourceAsStream("/site-to-merge-2.xml"));
    Config conf = new ConfigParser(getClass().getResourceAsStream("/merge-config.xml")).parse();
    SiteLoader loader = new SiteLoader() {
        @Override public boolean isUpToDate(URL url, long timestamp) { return false; }
        @Override public Site loadSite(URL url) {
            System.out.println();
            if (url.getPath().contains("site1")) return site1;
            else if (url.getPath().contains("site2")) return site2;
            return null;
        }
    };
    Site expected = Site.load(getClass().getResourceAsStream("/merged-site.xml"));

    @Test
    public void testFederateSites() {
        Federator f = new Federator();
        Site federated = f.federateSites("merged", loader, conf);
        assertNotNull(federated);
        assertTrue(expected.deepEquals(federated));
    }
}
