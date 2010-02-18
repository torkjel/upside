package upside.utils;

public final class Exceptions {
    private Exceptions() { }

    public static RuntimeException re(Exception e) {
        if (e instanceof RuntimeException)
            throw (RuntimeException)e;
        else
            throw new RuntimeException(e);
    }
}
