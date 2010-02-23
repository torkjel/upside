package upside.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Utils {

    private Utils() { }

    public static URL asUrl(String urlSpec) {
        try {
            return new URL(urlSpec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL asUrl(File f) {
        try {
            return f.toURI().toURL();
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

    public static void pipe(InputStream in, OutputStream out) {
        byte[] data = new byte[4096];
        try {
            int len;
            while ((len = in.read(data)) != -1)
                out.write(data, 0, len);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try { in.close(); } catch (IOException e) { e.printStackTrace(); }
            try { out.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }


    public static String hash(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(data);
        return toHex(hash);
    }

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(nibbleToHex(b >>> 4));
            sb.append(nibbleToHex(b));
        }
        return sb.toString();
    }

    private static char nibbleToHex(int b) {
        b &= 0xf;
        return (char)(b > 9 ? 'a' + (b - 10) : '0' + b);
    }

    public static boolean eq(Object o1, Object o2) {
        return (o1 != null && o1.equals(o2)) || (o1 == null && o2 == null);
    }

    public static File saveAsTempFile(InputStream is) {
        File f;
        try {
            f = File.createTempFile("upside", "xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveFile(f, is);
        return f;
    }

    public static void saveFile(File file, InputStream is) {
        OutputStream os;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pipe(is, os);
    }

}
