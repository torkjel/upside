package upside.server;

import java.io.OutputStream;
import java.net.URI;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upside.federation.Config;
import upside.federation.ConfigParser;
import upside.federation.Federator;
import upside.site.Site;
import upside.site.SiteLoader;
import upside.utils.Exceptions;

public class DispatcherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Request : " + request.getRequestURI());

            String path = new URI(request.getRequestURI()).getPath();

            if (path.endsWith(Site.SITEXML)) {
                doSite(path, response.getOutputStream());
            } else if (path.endsWith("/")) {
                doSite(path + Site.SITEXML, response.getOutputStream());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw Exceptions.re(e);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String getSiteName(String path) {
        String[] s = path.split("/");
        return s[s.length - 2];
    }

    private void doSite(String path, OutputStream out) {
        Config config = new ConfigParser(getClass().getResourceAsStream("/upside-conf.xml")).parse();
        SiteLoader loader = new SiteLoader();
        Federator federator = new Federator(loader, config);

        String siteName = getSiteName(path);
        Site merged = federator.federateSites(siteName);
        if (merged == null)
            throw new IllegalArgumentException("Site not found: " + siteName);
        try {
            out.write(merged.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            throw Exceptions.re(e);
        }
    }
}
