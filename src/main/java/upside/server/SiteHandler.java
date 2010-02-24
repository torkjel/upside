package upside.server;

import java.io.OutputStream;

public class SiteHandler extends PageHandler {

    private String siteName;

    public SiteHandler(OutputStream out, String siteName) {
        super(out);
        this.siteName = siteName;
    }

    @Override
    public void handle() {
        write(getSite(siteName).toString());
    }

}
