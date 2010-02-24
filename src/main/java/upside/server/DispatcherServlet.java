package upside.server;

import java.io.OutputStream;
import java.net.URI;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upside.utils.Exceptions;

public class DispatcherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Request : " + request.getRequestURI());


            OutputStream out = response.getOutputStream();
            Path p = new Path(new URI(request.getRequestURI()).getPath());

            if (!p.isValid()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            getHandler(p, out).handle();

        } catch (Exception e) {
            e.printStackTrace();
            throw Exceptions.re(e);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private PageHandler getHandler(Path path, OutputStream out) {
        String siteName = path.getSiteName();
        if (path.isSite())
            return new SiteHandler(out, siteName);
        else if (path.isSiteIndex())
            return new SiteIndexHandler(out, siteName);
        else if (path.isToplevelIndex())
            return new ToplevelIndexHandler(out);
        else
            throw new IllegalStateException();
    }

}

