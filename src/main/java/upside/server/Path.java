package upside.server;

import upside.site.Site;

class Path {

    private static final String INDEX = "index.hml";
    private static final String APP_NAME = "upside";

    private String[] path;

    private String siteName;

    private boolean isSite;
    private boolean isSiteIndex;
    private boolean isToplevelIndex;

    public Path(String path) {
        this.path = path.split("/");
        parse();
    }

    public boolean isSite() {
        return isSite;
    }

    public boolean isSiteIndex() {
        return isSiteIndex;
    }

    public boolean isToplevelIndex() {
        return isToplevelIndex;
    }

    public String getSiteName() {
        return siteName;
    }

    public boolean isValid() {
        return isSite || isSiteIndex || isToplevelIndex;
    }

    private void parse() {
        for (int n = 0; n < path.length; n++)
            if (path[n].equals(APP_NAME)) {
                if (n == path.length - 1 || (n == path.length - 2 && path[n + 1].equals(INDEX))) {
                    isToplevelIndex = true;
                    return;
                } else if (n == path.length - 2) {
                    siteName = path[n + 1];
                    isSiteIndex = true;
                    return;
                } else if (n == path.length - 3) {
                    siteName = path[n + 1];
                    if (path[n + 2].equals(Site.SITEXML))
                        isSite = true;
                    else if (path[n + 2].equals(INDEX))
                        isSiteIndex = true;
                }
            }
    }
}
