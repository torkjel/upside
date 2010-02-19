package upside.utils;

public final class Exceptions {
    private Exceptions() { }

    public static RuntimeException re(Exception e) {
        return e instanceof RuntimeException
            ? (RuntimeException)e
            : new RuntimeException(e);
    }
}
