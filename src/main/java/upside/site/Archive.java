package upside.site;

import java.net.URL;

import upside.utils.Utils;

public class Archive {

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

    public boolean equals(Object o) {
        // eclipse uses the path as a key, so that defines identity.
        // TODO: it's possible that the same path is used by different plug-ins. This can
        // only be worked around by taking control over features.xml and re-mapping the
        // plugin paths.
        if (o == this) return true;
        return (o instanceof Archive)
            && ((Archive)o).getPath().equals(getPath());
    }

    public int hashCode() {
        return getPath().hashCode();
    }
}
