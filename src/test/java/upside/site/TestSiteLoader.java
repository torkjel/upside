package upside.site;

import java.net.URL;

@org.junit.Ignore
public class TestSiteLoader implements SiteLoader {

    public int sitesLoaded = 0;
    public boolean isUpToDate = false;

    @Override
    public boolean isUpToDate(URL url, long timestamp) {
        return isUpToDate;
    }

    @Override
    public Site loadSite(URL url) {
        sitesLoaded++;
        if (url.toString().contains("site1"))
            return Site.load(getClass().getResource("/site-to-merge-1.xml"));
        else if (url.toString().contains("site2"))
            return Site.load(getClass().getResource("/site-to-merge-2.xml"));
        else
            throw new IllegalArgumentException(url.toString());
    }
}
