package upside.site;

import java.net.URL;

public interface SiteLoader {
    Site loadSite(URL url);
    boolean isUpToDate(URL url, long timestamp);
}
