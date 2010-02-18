package upside.site;

import java.net.URL;


public class SiteLoader {
    public Site loadSite(URL url) {
        return Site.load(url).withAbsoluteUrls(url);
    }
}
