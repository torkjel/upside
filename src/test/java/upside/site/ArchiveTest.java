package upside.site;

import static junit.framework.TestCase.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class ArchiveTest extends SiteTestBase {

    public static final String FOO_ARCH = "plugins/foo_1.0.jar";
    public static final String NOO_ARCH = "plugins/noo_1.3.jar";

    private final Site s = loadSite();

    @Test
    public void testParse() {
        assertEquals(4, s.getArchives().size());

        Archive a = s.getArchive(FOO_ARCH);
        assertEquals(a.getPath(), FOO_ARCH);
        assertEquals(a.getUrl(), URL + FOO_ARCH);
    }

    @Test
    public void testAbsoluteUrl() throws MalformedURLException {
        Archive a = s.getArchive(FOO_ARCH);
        URL url = new URL(URL);
        assertTrue(a.hasAbsoluteUrl());
        try {
            a.withAbsoluteUrl(url);
            fail();
        } catch (IllegalStateException e) {
            // OK!
        }

        Archive b = s.getArchive(NOO_ARCH);
        assertFalse(b.hasAbsoluteUrl());
        Archive abs = b.withAbsoluteUrl(url);
        assertEquals(URL + NOO_ARCH, abs.getUrl());
    }

    @Test
    public void testEquals() {
        Archive a1 = new Archive("a", "a");
        Archive a2 = new Archive("a", "b");
        Archive a3 = new Archive("b", "a");
        assertTrue(a1.equals(a1) && a1.equals(a2) && a2.equals(a1));
        assertFalse(a1.equals(a3) && a2.equals(a1));

        assertEquals(a1.hashCode(), a2.hashCode());
        assertFalse(a1.hashCode() == a3.hashCode());
    }
}
