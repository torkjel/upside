package upside.server;

import java.io.OutputStream;
import java.net.URI;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upside.federation.FederationManager;
import upside.site.Site;
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
        String siteName = getSiteName(path);
        Site merged = FederationManager.getinstance().getFederatedSite(siteName);

        if (merged == null)
            throw new IllegalArgumentException("Site not found: " + siteName);
        try {
            out.write(merged.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            throw Exceptions.re(e);
        }
    }
}
