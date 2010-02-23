package upside.site;

import java.io.InputStream;

public class SiteFactoryImpl implements SiteFactory {

    @Override
    public Site create(InputStream in) {
        return Site.load(in);
    }

}
