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
package upside.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class UtilsTest {

    @Test
    public void testHash() throws UnsupportedEncodingException {
        assertEquals(
            "e1449548860671bcfed5a5d1e4701532a0308d20",
            Utils.hash("hash this".getBytes("UTF-8")));
    }

    @Test
    public void pipeTest() throws UnsupportedEncodingException {
        String data = "pipe this!";
        ByteArrayInputStream bin = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Utils.pipe(bin, bos);
        assertEquals(data, bos.toString("UTF-8"));
    }
}
