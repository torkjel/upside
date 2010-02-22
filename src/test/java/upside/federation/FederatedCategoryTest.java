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
