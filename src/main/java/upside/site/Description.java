package upside.site;

import java.net.URL;

import upside.utils.Utils;

public class Description {
    private URL url;
    private String description;

    public Description(URL url, String description) {
        this.url = url;
        this.description = description;
    }

    public Description(String url, String description) {
        this(Utils.asUrl(url), description);
    }

    public String getDescription() {
        return description;
    }

    public URL getUrl() {
        return url;
    }
}
