package upside.site;

import java.util.HashSet;

public abstract class SiteTestBase {

    protected static final String URL = "http://foo.bar/";

    protected static final String SITE_RESOURCE = "/test-site.xml";
    protected static final java.net.URL SITE_URL = SiteTestBase.class.getResource(SITE_RESOURCE);

    private Site site;
    private Site site2;

    protected synchronized Site loadSite() {
        return site == null ? site = loadSite(SITE_RESOURCE) : site;
    }

    protected Site loadOtherSite() {
        return site2 == null ? site2 = loadSite("/test-site2.xml") : site2;
    }

    private Site loadSite(String resource) {
        return Site.load(getClass().getResource(resource));
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
