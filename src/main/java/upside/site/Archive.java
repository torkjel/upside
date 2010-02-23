package upside.site;

import java.net.URL;

import upside.utils.Utils;

public class Archive extends AbstractSiteElement {

    private String path;
    private String url;

    public Archive(String path, String url) {
        this.path = path;
        this.url = url;
    }

    public Archive withAbsoluteUrl(URL base) {
        if (hasAbsoluteUrl())
            throw new IllegalStateException("URL is already absolute");
        return new Archive(path, Utils.absoluteUrl(base, url));
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasAbsoluteUrl() {
        return Utils.isAbsolute(url);
    }

    @Override
    public boolean equals(Object o) {
        // eclipse uses the path as a key, so that defines identity.
        // XXX: it's possible that the same path is used by different plug-ins. This can
        // only be worked around by taking control over features.xml and re-mapping the
        // plugin paths.
        return maybeEquals(o) && ((Archive)o).getPath().equals(getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Archive a = (Archive)se;
            return eq(getUrl(), a.getUrl())
                && eq(getPath(), a.getPath());
        }
        return false;
    }
}
