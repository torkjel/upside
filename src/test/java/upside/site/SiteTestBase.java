package upside.site;

import java.util.HashSet;

public abstract class SiteTestBase {

    protected static final String URL = "http://foo.bar/";

    private Site site;

    protected synchronized Site loadSite() {
        return site == null
            ? site = Site.load(getClass().getResource("/test-site.xml"))
            : site;
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
