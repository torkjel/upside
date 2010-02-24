/*
 * Copyright 2010 Torkjel Hongve (torkjelh@conduct.no)
 *
 * This file is part of Upside.
 *
 * Upside is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Upside is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Upside.  If not, see <http://www.gnu.org/licenses/>.
 */
package upside.server;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class PathTest {

    private static final boolean F = false, T = true;
    int valid = 1, tli = 2, si = 3, site = 4, siteName = 5;
    Object[][] data = {
        { "upside/",                            T, T, F, F, null },
        { "foo/bar/upside/",                    T, T, F, F, null },
        { "foo/bar/upside",                     T, T, F, F, null },
        { "foo/bar/upside/index.html",          T, T, F, F, null },

        { "foo/bar/upside/asite/",              T, F, T, F, "asite" },
        { "foo/bar/upside/asite",               T, F, T, F, "asite" },
        { "foo/bar/upside/asite/index.html",    T, F, T, F, "asite" },
        { "foo/bar/upside/asite/asdfasdasdf",   F, F, F, F, null },

        { "upside/asite/site.xml",              T, F, F, T, "asite" },
        { "foo/bar/upside/asite/site.xml",      T, F, F, T, "asite" },
        { "foo/bar/upside/asite/site.xml/foo",  F, F, F, F, null },

        { "downside/foo/index.html",            F, F, F, F, null },
        { "upside/foo/bar/",                    F, F, F, F, null },
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
            assertEquals(path, d[siteName], p.getSiteName());
        }
    }

}
