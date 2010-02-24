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
package upside.federation;

import java.util.Set;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class FederatedCategoryTest extends FederationTestBase {

    @Test
    public void testFederatedCategories() {
        Config conf = loadConf();
        FederatedSite main = conf.getFederatedSite("main");
        Set<FederatedCategory> categories = main.getCategories();

        int checksum = 0;
        for (FederatedCategory fc : categories) {
            if ("cat-foo".equals(fc.getName())) {
                assertEquals("Foo stuff", fc.getDescription());
                assertEquals(4, fc.getIncludes().size());
                checksum += 1;
            } else if ("cat-bar".equals(fc.getName())) {
                assertNull(fc.getDescription());
                assertEquals(1, fc.getIncludes().size());
                checksum += 2;
            } else {
                checksum += 4;
            }
        }
        assertEquals(3, checksum);

        FederatedSite test = conf.getFederatedSite("test");
        assertEquals(1, test.getCategories().size());
    }
}
