package upside.site;

import java.net.URL;

public class SimpleSiteLoader implements SiteLoader {

    @Override
    public Site loadSite(URL url) {
        return Site.load(url).withAbsoluteUrls(url);
    }

    @Override
    public boolean isUpToDate(URL url, long timestamp) {
        return false;
    }
}
