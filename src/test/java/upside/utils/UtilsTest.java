package upside.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import static junit.framework.TestCase.*;

public class UtilsTest {

    @Test
    public void testHash() throws UnsupportedEncodingException {
        assertEquals(
            "e1449548860671bcfed5a5d1e4701532a0308d20",
            Utils.hash("hash this".getBytes("UTF-8")));
    }

    @Test
    public void pipeTest() throws UnsupportedEncodingException {
        String data = "pipe this!";
        ByteArrayInputStream bin = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Utils.pipe(bin, bos);
        assertEquals(data, bos.toString("UTF-8"));
    }
}
