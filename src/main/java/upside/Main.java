package upside;

import java.net.MalformedURLException;
import java.net.URL;

import upside.site.Site;

public class Main {

    public static void main(String[] args) {
        new Main().go();
    }

    public Main() {

    }

    public void go() {
        try {
            System.out.println(Site.load(new URL("http://andrei.gmxhome.de/eclipse/site.xml")));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}


