package upside.server;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class PathTest {

    private static final boolean F = false, T = true;
    int valid = 1, tli = 2, si = 3, site = 4;
    Object[][] data = {
        { "upside/",                            T, T, F, F },
        { "foo/bar/upside/",                    T, T, F, F },
        { "foo/bar/upside",                     T, T, F, F },
        { "foo/bar/upside/index.html",          T, T, F, F },

        { "foo/bar/upside/asite/",              T, F, T, F },
        { "foo/bar/upside/asite",               T, F, T, F },
        { "foo/bar/upside/asite/index.html",    T, F, T, F },
        { "foo/bar/upside/asite/asdfasdasdf",   F, F, F, F },

        { "upside/asite/site.xml",              T, F, F, T },
        { "foo/bar/upside/asite/site.xml",      T, F, F, T },
        { "foo/bar/upside/asite/site.xml/foo",  F, F, F, F },

        { "downside/foo/index.html",            F, F, F, F },
        { "upside/foo/bar/",                    F, F, F, F },
    };


    @Test
    public void testPath() {

        for (Object[] d : data) {
            String path = (String)d[0];
            Path p = new Path(path);
            assertEquals(path, d[valid], p.isValid());
            assertEquals(path, d[tli], p.isToplevelIndex());
            assertEquals(path, d[si], p.isSiteIndex());
            assertEquals(path, d[site], p.isSite());
        }
    }

}
