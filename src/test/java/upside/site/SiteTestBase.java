package upside.site;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public abstract class SiteTestBase {

    protected static final String URL = "http://foo.bar/";

    protected Site loadSite() {
        try {
            return Site.load(
                new URL(URL),
                getClass().getResourceAsStream("/test-site.xml"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    protected Category cat(String name) {
        return new Category(name, name, name);
    }

    protected Feature feat(String id, String version) {
        return new Feature(
            "feature/" + id + "_" + version + ".jar",
            id, version,
            false, new HashSet<Category>());
    }

}
