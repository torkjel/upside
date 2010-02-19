package upside.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class Utils {

    private Utils() { }

    public static URL asUrl(String urlSpec) {
        try {
            return new URL(urlSpec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAbsolute(String path) {
        return URI.create(path).isAbsolute();
    }

    public static String absoluteUrl(URL base, String path) {
        if (isAbsolute(path))
            return path;
        else
            try {
                return new URI(
                    base.getProtocol(), null, base.getHost(),
                    base.getPort(), base.getPath() + path, null, null).toString();
            } catch (URISyntaxException e) {
                throw Exceptions.re(e);
            }
    }


}
