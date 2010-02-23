package upside.site;

import java.io.InputStream;

public interface SiteFactory {
    Site create(InputStream in);
}
