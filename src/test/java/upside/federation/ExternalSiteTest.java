package upside.federation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class ExternalSiteTest extends FederationTestBase {

    @Test
    public void testExternalSites() throws MalformedURLException {
        Config conf = loadConf();

        assertNotNull(conf);

        assertEquals("http://upside.example.com/", conf.getBaseUrl());

        Set<String> names = conf.getOriginSiteNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("foo"));
        assertTrue(names.contains("bar"));

        for (String name : names) {
            if (name.equals("foo")) {
                URL u = new URL("http://foo.example.com/site/");
                assertEquals(u, conf.getOriginSiteUrl(name));
                assertEquals(u, conf.getOriginSites().get(name));
            } else {
                URL u = new URL("http://bar.example.com/update-site/");
                assertEquals(u, conf.getOriginSiteUrl(name));
                assertEquals(u, conf.getOriginSites().get(name));
            }
        }
    }
}
