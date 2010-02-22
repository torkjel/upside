package upside.utils;

import org.xml.sax.SAXParseException;

public final class Exceptions {
    private Exceptions() { }

    public static RuntimeException re(Exception e) {
        if (e instanceof RuntimeException)
            return (RuntimeException)e;
        else if (e instanceof SAXParseException) {
            SAXParseException sax = (SAXParseException)e;
            return new RuntimeException(sax.getMessage() + "(" +
                "System:" + sax.getSystemId() + ", " +
                "Public:" + sax.getPublicId() + ", " +
                "L:" + sax.getLineNumber() + " C:" + sax.getColumnNumber() + ")",
                e);
        } else
            return new RuntimeException(e);
    }
}
